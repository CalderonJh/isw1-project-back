package fpc.app.dto.app;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record MatchDTO(
    @Positive Long matchId,
    @Positive Long awayClubId,
    @Positive Long stadiumId,
    @Min(2000) Integer year,
    @Min(1) @Max(2) Integer season,
    @Future LocalDateTime matchDate) {}
