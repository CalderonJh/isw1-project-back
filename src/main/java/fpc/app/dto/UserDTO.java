package fpc.app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import lombok.Builder;

@Builder
@Schema(description = "User registration data")
public record UserDTO(
    Long userId,
    @NotBlank @Size(min = 3, max = 250) String name,
    @NotBlank @Size(min = 3, max = 250) String lastName,
    @Email @NotBlank @Size(min = 5, max = 250) String email,
    @NotNull @Min(1) Long documentTypeId,
    @NotBlank
        @Size(min = 6, max = 25)
        @Pattern(regexp = "^\\d+$")
        @Schema(description = "Must be a number", example = "12345678")
        String documentNumber,
    @Pattern(regexp = "^([MF])$") @Schema(example = "M") String gender,
    @Past @NotNull LocalDate birthDate,
    @Size(min = 7, max = 10) @Pattern(regexp = "^\\d+$") String phoneNumber,
    @NotBlank
        @Size(min = 6, max = 250)
        @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,250}$",
            message =
                "Password must contain at least one uppercase letter, one lowercase letter, one number, and be between 6 and 250 characters long")
        @Schema(example = "Hello1234")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String password) {}
