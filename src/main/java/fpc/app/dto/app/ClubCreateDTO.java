package fpc.app.dto.app;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record ClubCreateDTO(
    @Schema(description = "Club name", example = "Football Club Barcelona") @NotBlank String name,
    @Schema(description = "Short name", example = "Barcelona") @NotBlank String shortName) {}
