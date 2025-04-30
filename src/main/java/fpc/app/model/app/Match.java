package fpc.app.model.app;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.*;

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

	@NotNull
	@Column(name = "year", nullable = false)
	private Integer year;

	@NotNull
	@Column(name = "season", nullable = false)
	@Min(1)
	@Max(2)
	private Integer season;

	@ManyToOne
	@NotNull
	@JoinColumn(name = "stadium_id", nullable = false)
	private Stadium stadium;

  @Column(name = "start_time", nullable = false)
  private LocalDateTime startDate;
}
