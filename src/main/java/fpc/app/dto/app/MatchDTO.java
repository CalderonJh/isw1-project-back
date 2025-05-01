package fpc.app.dto.app;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MatchDTO(
    @Positive @JsonProperty("match_id") Long matchId,
    @Positive @JsonProperty("away_club") Long awayClubId,
    @Positive @JsonProperty("stadium_id") Long stadiumId,
    @Min(2000) Integer year,
    @Min(1) @Max(2) Integer season,
    @Future @JsonProperty("match_date") LocalDateTime matchDate) {}
