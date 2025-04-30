package fpc.app.dto.app;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubRequest {
  @JsonProperty("name")
  @Schema(description = "Club name", example = "Football Club Barcelona")
  private String name;

  @JsonProperty("short_name")
  @Schema(description = "Short name", example = "Barcelona")
  private String shortName;
}
