package fpc.app.model.auth;

import fpc.app.dto.util.Reference;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role", schema = "app")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 100)
	private String name;

  @Override
  public final boolean equals(Object o) {
    if (!(o instanceof Role role)) return false;

    return Objects.equals(id, role.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  public Reference getReference() {
    return new Reference(id, name);
  }
}
