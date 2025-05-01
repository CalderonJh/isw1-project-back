package fpc.app.dto.app;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubDTO {
  private Long id;
  private String name;

  private String shortName;

  private String imageId;
}
