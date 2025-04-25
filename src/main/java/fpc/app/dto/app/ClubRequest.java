package fpc.app.dto.app;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubRequest {
  private String name;
  private String shortName;
}
