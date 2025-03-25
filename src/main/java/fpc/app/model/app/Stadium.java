package fpc.app.model.app;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import jakarta.persistence.*;

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

	@Column(name = "name", nullable = false, length = 250)
	private String name;
}
