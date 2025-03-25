package fpc.app.model.app;

import fpc.app.model.auth.User;
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
@Table(name = "club_admin", schema = "app")
public class ClubAdmin {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "club_id")
	private Club club;

	@NotNull
	@Column(name = "start_date", nullable = false)
	private Date startDate;

	@Column(name = "end_date")
	private Date endDate;
}
