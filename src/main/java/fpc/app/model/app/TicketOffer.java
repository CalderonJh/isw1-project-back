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
  @Column(name = "posted_by", nullable = false)
  private Long publisherId;

	@NotNull
	@Column(name = "club_id", nullable = false)
	private Long clubId;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "match_id")
	private Match match;
}
