package fpc.app.model.app;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ticket_offer", schema = "app")
public class TicketOffer extends Offer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "posted_by", nullable = false)
  private ClubAdmin publisher;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "match_id")
	private Match match;
}
