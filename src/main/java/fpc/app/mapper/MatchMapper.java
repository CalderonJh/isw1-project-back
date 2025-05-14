package fpc.app.mapper;

import fpc.app.dto.response.MatchResponseDTO;
import fpc.app.dto.util.Suggestion;
import fpc.app.model.app.Match;
import java.util.List;

public class MatchMapper {
  private MatchMapper() {}

  public static MatchResponseDTO toResponseDTO(Match match) {
    return MatchResponseDTO.builder()
        .matchId(match.getId())
        .awayClub(new Suggestion(match.getAwayClub().getId(), match.getAwayClub().getShortName()))
        .year(match.getYear())
        .season(match.getSeason())
        .stadium(new Suggestion(match.getStadium().getId(), match.getStadium().getName()))
        .matchDate(match.getStartDate())
        .build();
  }

  public static List<MatchResponseDTO> toResponseDTO(List<Match> matches) {
    return matches.stream().map(MatchMapper::toResponseDTO).toList();
  }
}
