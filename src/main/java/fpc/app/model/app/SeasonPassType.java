package fpc.app.model.app;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "season_pass_type", schema = "app")
public class SeasonPassType {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "season_pass_offer_id", nullable = false)
	private Long seasonPassOfferId;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "stand_id", nullable = false)
	private Stand stand;

	@NotNull
	@Column(name = "price", nullable = false)
	private BigDecimal price;
}
