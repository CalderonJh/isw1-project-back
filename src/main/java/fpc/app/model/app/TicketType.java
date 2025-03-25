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
@Table(name = "ticket_type", schema = "app")
public class TicketType {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "ticket_offer_id")
	private TicketOffer ticketOffer;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "stand_id")
	private Stand stand;

	@NotNull
	@Column(name = "price", nullable = false)
	private BigDecimal price;
}
