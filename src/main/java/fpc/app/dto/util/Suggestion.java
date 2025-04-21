package fpc.app.dto.util;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Suggestion {
  private Object id;
  private String description;
}
