package fpc.app.model.app;

import fpc.app.model.auth.User;
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
@Table(name = "season_pass_holder", schema = "app")
public class SeasonPassHolder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "season_pass_type_id", nullable = false)
	private SeasonPassType seasonPassType;

	@NotBlank
	@Column(name = "payment_id", nullable = false)
	private String paymentId;
}
