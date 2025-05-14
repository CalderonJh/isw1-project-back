package fpc.app.dto.response;

import fpc.app.dto.util.Suggestion;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record MatchResponseDTO(
    Long matchId,
    Suggestion awayClub,
    Suggestion stadium,
    Integer year,
    Integer season,
    LocalDateTime matchDate) {}
