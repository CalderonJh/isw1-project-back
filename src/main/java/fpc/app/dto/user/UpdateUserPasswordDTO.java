package fpc.app.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserPasswordDTO(
    @NotBlank
        @Size(min = 6, max = 250)
        @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,250}$",
            message =
                "Password must contain at least one uppercase letter, one lowercase letter, one number, and be between 6 and 250 characters long")
        @Schema(example = "Hello1234")
        String password) {}
