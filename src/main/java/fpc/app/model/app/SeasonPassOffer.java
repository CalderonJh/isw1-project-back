package fpc.app.model.app;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "season_pass_offer", schema = "app")
public class SeasonPassOffer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "posted_by", nullable = false)
	private ClubAdmin postedBy;

	@NotNull
	@Column(name = "year", nullable = false)
	private Integer year;

	@NotNull
	@Column(name = "season", nullable = false)
	@Min(1)
	@Max(2)
	private Integer season;

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
