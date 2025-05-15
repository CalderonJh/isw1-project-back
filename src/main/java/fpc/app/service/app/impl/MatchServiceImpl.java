package fpc.app.service.app.impl;

import static fpc.app.util.Tools.getColTime;
import static java.util.Objects.isNull;

import fpc.app.constant.MatchSearchType;
import fpc.app.dto.request.MatchCreationDTO;
import fpc.app.exception.DataNotFoundException;
import fpc.app.exception.ValidationException;
import fpc.app.model.app.Club;
import fpc.app.model.app.Match;
import fpc.app.model.app.Stadium;
import fpc.app.repository.app.MatchRepository;
import fpc.app.service.app.ClubService;
import fpc.app.service.app.MatchService;
import fpc.app.service.app.StadiumService;
import jakarta.validation.constraints.Future;
import java.time.LocalDateTime;
import java.util.List;
import lombok.NonNull;
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
    return this.matchRepository
        .findById(id)
        .orElseThrow(() -> new DataNotFoundException("Partido no encontrado"));
  }

  @Override
  public Match save(Match match) {
    return this.matchRepository.save(match);
  }

  @Override
  public Match create(Club homeClub, MatchCreationDTO dto) {
    validateAwayClub(homeClub, dto.awayClubId());
    Stadium stadium = stadiumService.getStadium(dto.stadiumId());
    Club awayClub = clubService.getClub(dto.awayClubId());
    validateMatchDay(homeClub.getId(), dto.matchDate());
    Match toSave =
        Match.builder()
            .homeClub(homeClub)
            .awayClub(awayClub)
            .year(dto.year())
            .season(dto.season())
            .stadium(stadium)
            .startDate(dto.matchDate())
            .build();

    return matchRepository.save(toSave);
  }

  private void validateMatchDay(Long clubId, @Future LocalDateTime localDateTime) {
    LocalDateTime start = localDateTime.toLocalDate().atStartOfDay();
    LocalDateTime end = start.plusDays(1);
    if (matchRepository.existsByDay(clubId, start, end))
      throw new ValidationException("Ya existe un partido para ese día");
  }

  private void validateAwayClub(Club club, Long awayTeamId) {
    if (club.getId().equals(awayTeamId)) {
      throw new ValidationException("El equipo visitante es el mismo que el local");
    }
  }

  @Override
  public void update(Long matchId, MatchCreationDTO dto) {
    Match match = getMatch(dto.matchId());
    Club club = match.getHomeClub();
    if (dto.stadiumId().equals(match.getStadium().getId())) {
      Stadium stadium = stadiumService.getStadium(dto.stadiumId());
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
  public List<Match> getMatches(@NonNull Club club, MatchSearchType searchType, Long stadiumId) {
    if (searchType.equals(MatchSearchType.SEASON_PASS) && isNull(stadiumId))
      throw new ValidationException("Se debe definir un estadio");
    return switch (searchType) {
      case ALL -> matchRepository.findByHomeClubId(club.getId(), stadiumId);
      case TICKET -> matchRepository.findValidMatchesForTicketsOffer(club.getId(), getColTime(), stadiumId);
      case SEASON_PASS -> matchRepository.findByHomeClubIdAndFuture(club.getId(), getColTime(), stadiumId);
    };
  }

  @Override
  public void deleteMatch(Long id) {
    matchRepository.deleteById(id);
  }
}
