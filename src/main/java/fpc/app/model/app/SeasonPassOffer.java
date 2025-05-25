package fpc.app.model.app;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "season_pass_offer", schema = "app")
public class SeasonPassOffer extends Offer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

  @NotNull
  @Column(name = "posted_by", nullable = false)
  private Long publisherId;

  @NotNull
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "stadium_id", nullable = false)
  private Stadium stadium;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "club_id", nullable = false)
  private Club club;

  @NotNull
  @Column(name = "description", nullable = false)
  private String description;

	@NotNull
	@Column(name = "year", nullable = false)
	private Integer year;

	@NotNull
	@Column(name = "season", nullable = false)
	@Min(1)
	@Max(2)
	private Integer season;
	
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "season_pass_offer_match",
      schema = "app",
      joinColumns = @JoinColumn(name = "sp_offer_id"),
      inverseJoinColumns = @JoinColumn(name = "match_id"))
	@Builder.Default
  private List<Match> matches = new ArrayList<>();

  public void addMatch(Match match) {
    if (matches == null) {
      matches = new ArrayList<>();
    }
    matches.add(match);
  }

  public void removeMatch(Long matchId) {
    if (matches != null) {
      matches.removeIf(m -> m.getId().equals(matchId));
    }
  }
}
