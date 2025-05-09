package fpc.app.service.app.impl;

import static fpc.app.util.Tools.requireData;
import static java.util.Objects.requireNonNull;

import fpc.app.dto.app.MatchDTO;
import fpc.app.exception.ValidationException;
import fpc.app.model.app.Club;
import fpc.app.model.app.Match;
import fpc.app.model.app.Stadium;
import fpc.app.repository.app.MatchRepository;
import fpc.app.service.app.ClubService;
import fpc.app.service.app.MatchService;
import fpc.app.service.app.StadiumService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

  private final MatchRepository matchRepository;
  private final ClubService clubService;
  private final StadiumService stadiumService;

  @Override
  public Match getMatch(Long id) {
    return this.matchRepository.findById(id).orElse(null);
  }

  @Override
  public Match save(Match match) {
    return this.matchRepository.save(match);
  }

  @Override
  public Match create(String username, MatchDTO dto) {
    Club club = clubService.getClubByAdmin(username);
    validateAwayClub(club, dto.awayClubId());
    Stadium stadium = stadiumService.getStadium(username, dto.stadiumId());
    validateStadium(requireNonNull(stadium), club);
    Match toSave =
        Match.builder()
            .homeClub(club)
            .awayClub(clubService.getClub(dto.awayClubId()))
            .year(dto.year())
            .season(dto.season())
            .stadium(stadium)
            .build();

    return matchRepository.save(toSave);
  }

  private void validateStadium(Stadium stadium, Club club) {
    if (!stadium.getClub().getId().equals(club.getId())) {
      throw new ValidationException("El estadio no pertenece al club");
    }
  }

  private void validateAwayClub(Club club, Long awayTeamId) {
    if (club.getId().equals(awayTeamId)) {
      throw new ValidationException("El equipo visitante es el mismo que el local");
    }
  }

  @Override
  public void update(String username, MatchDTO dto) {
    Club club = clubService.getClubByAdmin(username);
    Match match = requireData(getMatch(dto.matchId()));
    if (dto.stadiumId().equals(match.getStadium().getId())) {
      Stadium stadium = requireData(stadiumService.getStadium(username, dto.stadiumId()));
      validateStadium(stadium, club);
      match.setStadium(stadium);
    }
    if (!dto.awayClubId().equals(match.getAwayClub().getId())) {
      validateAwayClub(club, dto.awayClubId());
      match.setAwayClub(clubService.getClub(dto.awayClubId()));
    }
    match.setSeason(dto.season());
    match.setYear(dto.year());
    match.setStartDate(dto.matchDate());
    matchRepository.save(match);
  }

  @Override
  public List<Match> getMatches(String username) {
    Club club = clubService.getClubByAdmin(username);
    return matchRepository.findByHomeClubId(club.getId());
  }

  @Override
  public void deleteMatch(String username, Long id) {
    Club club = clubService.getClubByAdmin(username);
    Match match = matchRepository.findById(id).orElse(null);
    if (match != null && match.getHomeClub().getId().equals(club.getId()))
      matchRepository.delete(match);
    else throw new ValidationException("No se puede eliminar el partido");
  }
}
