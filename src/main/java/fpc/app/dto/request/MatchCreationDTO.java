package fpc.app.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record MatchCreationDTO(
    @NotNull @Positive Long matchId,
    @NotNull @Positive Long awayClubId,
    @NotNull @Positive Long stadiumId,
    @NotNull @Min(2000) Integer year,
    @NotNull @Min(1) @Max(2) Integer season,
    @Future LocalDateTime matchDate) {}
