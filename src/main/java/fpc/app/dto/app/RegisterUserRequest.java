package fpc.app.dto.app;

import com.fasterxml.jackson.annotation.JsonProperty;
import fpc.app.model.app.IdentityDocument;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserRequest {

  @NotBlank String name;

  @NotBlank
  @JsonProperty("last_name")
  String lastName;

  @NotBlank String gender;

  @NotNull
  @PastOrPresent
  @JsonProperty("birth_date")
  LocalDate birthDate;

  @NotNull
  @JsonProperty("doc_type")
  IdentityDocument identityDocument;

  @NotBlank
  @JsonProperty("doc_number")
  String documentNumber;

  @Email @NotBlank String email;

  @NotBlank String password;
  
  @NotBlank String phone;
}
