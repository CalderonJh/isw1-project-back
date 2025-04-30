package fpc.app.dto.app;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StandDTO {
  private String name;
  private Integer capacity;
}
