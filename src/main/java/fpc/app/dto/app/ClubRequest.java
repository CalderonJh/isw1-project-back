package fpc.app.dto.app;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record ClubRequest(
    @JsonProperty("name") @Schema(description = "Club name", example = "Football Club Barcelona")
        String name,
    @JsonProperty("short_name") @Schema(description = "Short name", example = "Barcelona")
        String shortName) {}
