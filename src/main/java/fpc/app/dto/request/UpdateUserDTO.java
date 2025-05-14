package fpc.app.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
@Schema(description = "User update data")
public record UpdateUserDTO(
    @NotBlank @Size(min = 3, max = 250) @Schema(description = "First and middle name") String name,
    @NotBlank @Size(min = 3, max = 250) @Schema(description = "Last name") String lastName,
    @Email @NotBlank @Size(min = 5, max = 250) String email,
    @Size(min = 7, max = 10) @Schema(description = "Must be a number", example = "12344321")
        String phoneNumber) {}
