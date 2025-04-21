package fpc.app.dto.app;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User registration data")
public class RegisterUserRequest {

  @NotBlank
  @Size(min = 3, max = 250)
  @Schema(description = "First and middle name")
  private String name;

  @NotBlank
  @Size(min = 3, max = 250)
  @Schema(description = "Last name")
  @JsonProperty("last_name")
  private String lastName;

  @Email
  @NotBlank
  @Size(min = 5, max = 250)
  private String email;

  @NotNull
  @Min(1)
  @JsonProperty("doc_type_id")
  @Schema(example = "1")
  private Long documentTypeId;

  @NotBlank
  @JsonProperty("doc_number")
  @Size(min = 6, max = 25)
  @Pattern(regexp = "^\\d+$")
  @Schema(description = "Must be a number", example = "12345678")
  private String documentNumber;

  @Pattern(regexp = "^([MF])$")
  @Schema(example = "M")
  private String gender;

  @Past
  @NotNull
  @JsonProperty("birth_date")
  @Schema(description = "Past date", example = "2000-01-01")
  private LocalDate birthDate;

  @Size(min = 7, max = 10)
  @JsonProperty("phone_number")
  @Schema(description = "Must be a number", example = "12344321")
  private String phoneNumber;

  @NotBlank
  @Size(min = 6, max = 250)
  @Pattern(
      regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,250}$",
      message =
          "Password must contain at least one uppercase letter, one lowercase letter, one number, and be between 6 and 250 characters long")
  @Schema(example = "Hello1234")
  private String password;
}
