package fpc.app.model.app;

import static fpc.app.util.Tools.equalsText;

import fpc.app.dto.util.Reference;
import fpc.app.exception.ValidationException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stadium", schema = "app")
public class Stadium {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "club_id")
	private Club club;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "stadium_id")
  @Builder.Default
  private List<Stand> stands = new ArrayList<>();

	@Column(name = "name", nullable = false, length = 250)
	private String name;

  @Column(name = "image_id", length = 250)
  private String imageId;

  public void addStand(Stand stand) {
    if (this.stands.stream().anyMatch(s -> equalsText(stand.getName(), s.getName())))
      throw new ValidationException("Ya existe una tribuna con el nombre " + stand.getName());

    this.stands.add(stand);
  }

  public void clearStands() {
    this.stands.clear();
  }

  public Stand getStand(String standName) {
    for (Stand stand : stands) if (equalsText(stand.getName(), standName)) return stand;
    return null;
  }

  public Reference getReference() {
    return new Reference(this.id, this.name);
  }
}
