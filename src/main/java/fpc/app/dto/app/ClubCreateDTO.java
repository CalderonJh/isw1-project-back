package fpc.app.dto.app;

import io.swagger.v3.oas.annotations.media.Schema;

public record ClubCreateDTO(
    @Schema(description = "Club name", example = "Football Club Barcelona") String name,
    @Schema(description = "Short name", example = "Barcelona") String shortName) {}
