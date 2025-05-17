package fpc.app.repository.app;

import fpc.app.model.app.TicketOffer;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TicketOfferRepository extends JpaRepository<TicketOffer, Long> {
  @Query("select o.publisher.club.id from TicketOffer o where o.id = :offerId")
  Long getClubId(Long offerId);

  @Query(
      "select o from TicketOffer o where o.publisher.club.id in :clubIds and o.paused = false  and o.startDate <= :now and o.endDate >= :now")
  List<TicketOffer> getOffersByClubIdIn(List<Long> clubIds, LocalDateTime now);

  @Query("select count(o.id) > 0 from TicketOffer o where o.match.id = :matchId")
  boolean existsMatchTicketOffer(Long matchId);

  @Query("select o from TicketOffer o where o.publisher.club.id = :clubId")
  List<TicketOffer> findByClubId(Long clubId);

  @Query(
      "select o from TicketOffer o where o.paused = false and o.startDate <= :now and o.endDate >= :now")
  List<TicketOffer> getAllActive(LocalDateTime now);
}
