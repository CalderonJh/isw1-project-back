package fpc.app.model.app;


import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "club", schema = "app")
public class Club {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

  @NotBlank
  @Column(name = "name", nullable = false, length = 250, unique = true)
  private String name;

  @Nullable
  @Column(name = "image_id", length = 250)
  private String imageId;

  @NotBlank
  @Column(name = "short_name", length = 50, unique = true)
  private String shortName;
}
