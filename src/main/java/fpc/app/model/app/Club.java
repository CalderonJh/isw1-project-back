package fpc.app.model.app;

import fpc.app.dto.util.Reference;
import fpc.app.model.auth.User;
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

  @Nullable
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "admin_id", unique = true)
  private User admin;

  public Reference getReference() {
    return new Reference(id, name);
  }
}
