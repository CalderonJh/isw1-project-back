package fpc.app.model.app;

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

	@NotNull
	@ManyToOne
	@JoinColumn(name = "stadium_id")
	private Stadium stadium;

	@NotBlank
	@Column(name = "name", nullable = false, length = 250)
	private String name;

	@NotNull
	@Column(name = "capacity", nullable = false)
	private Integer capacity;
}
