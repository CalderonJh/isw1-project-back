package fpc.app.dto.util;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Suggestion")
public class Suggestion {
  @Schema(description = "ID of the suggestion", example = "1")
  private Object id;
  private String description;
}
