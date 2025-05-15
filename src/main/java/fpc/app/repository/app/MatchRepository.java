package fpc.app.repository.app;

import fpc.app.model.app.Match;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MatchRepository extends JpaRepository<Match, Long> {
  @Query(
      "select m from Match m where m.homeClub.id = :homeClubId and :stadiumId is null or m.stadium.id = :stadiumId")
  List<Match> findByHomeClubId(Long homeClubId, Long stadiumId);

  @Query("select m.homeClub.id from Match m where m.id = :recordId")
  Long getHomeClubId(Long recordId);

  /**
   * Finds all future matches for a given club that have not been offered yet.
   *
   * @param id Home club id
   * @param colTime Current time
   * @param stadiumId Stadium id (optional)
   * @return List of matches
   */
  @Query(
      """
				select m
				from Match m
				         left join TicketOffer o on m.id = o.match.id
				where o.id is null
				  and m.homeClub.id = :id
				  and m.startDate > :colTime
				  and (:stadiumId is null or m.stadium.id = :stadiumId)
				""")
  List<Match> findValidMatchesForTicketsOffer(Long id, LocalDateTime colTime, Long stadiumId);

  @Query("select m from Match m where m.homeClub.id = :id and m.startDate > :colTime and :stadiumId is null or m.stadium.id = :stadiumId")
  List<Match> findByHomeClubIdAndFuture(Long id, LocalDateTime colTime, Long stadiumId);
}
