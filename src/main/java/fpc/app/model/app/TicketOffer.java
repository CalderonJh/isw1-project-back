package fpc.app.model.app;

import jakarta.annotation.Nullable;
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
@Table(name = "ticket_offer", schema = "app")
public class TicketOffer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "posted_by", nullable = false)
	private ClubAdmin postedBy;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "match_id")
	private Match match;

	@NotNull
	@Column(name = "start_date", nullable = false)
	private Date startDate;

	@NotNull
	@Column(name = "end_date", nullable = false)
	private Date endDate;

	@Nullable
	@Column(name = "image_id")
	private String imageId;
}
