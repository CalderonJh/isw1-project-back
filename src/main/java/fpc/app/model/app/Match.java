package fpc.app.model.app;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "match", schema = "app")
public class Match {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@NotNull
	@JoinColumn(name = "home_team_id", nullable = false)
	private Club homeClub;

	@ManyToOne
	@NotNull
	@JoinColumn(name = "away_team_id", nullable = false)
	private Club awayClub;

	@ManyToOne
	@NotNull
	@JoinColumn(name = "stadium_id", nullable = false)
	private Stadium stadium;

	@Column(name = "start_time", nullable = false)
	private Date startDate;
}
