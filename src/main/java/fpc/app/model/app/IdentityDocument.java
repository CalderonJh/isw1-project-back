package fpc.app.model.app;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "identity_document", schema = "app")
public class IdentityDocument {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Column(name = "name", nullable = false, unique = true, length = 100)
	private String name;

	@NotBlank
	@Column(name = "abbreviation", nullable = false, unique = true, length = 4)
	private String abbreviation;

	public IdentityDocument(Long id) {
		this.id = id;
	}
}
