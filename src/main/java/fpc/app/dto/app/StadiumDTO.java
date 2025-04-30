package fpc.app.dto.app;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StadiumDTO {
  @NotBlank
  @Size(min = 3, max = 250)
  private String name;

  @Size(min = 1, max = 25)
  private List<StandDTO> stands;
}
