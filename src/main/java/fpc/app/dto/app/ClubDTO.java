package fpc.app.dto.app;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubDTO {
  private Long id;
  private String name;

  @Column(name = "short_name")
  private String shortName;

  @JsonProperty("image_id")
  private String imageId;
}
