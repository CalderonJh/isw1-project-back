package fpc.app.model.app;

import fpc.app.dto.util.Reference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stand", schema = "app")
public class Stand {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Column(name = "name", nullable = false, length = 250)
	private String name;

	@NotNull
	@Column(name = "capacity", nullable = false)
	private Integer capacity;

	public Stand(String name, Integer capacity) {
		this.name = name;
		this.capacity = capacity;
	}

	public Stand(Long id) {
		this.id = id;
	}

	public Reference getReference() {
		return new Reference(id, name);
	}
}
