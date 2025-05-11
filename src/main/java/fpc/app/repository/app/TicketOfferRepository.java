package fpc.app.repository.app;

import fpc.app.model.app.TicketOffer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TicketOfferRepository extends JpaRepository<TicketOffer, Long> {
  @Query("select o.publisher.club.id from TicketOffer o where o.id = :offerId")
  Long getClubId(Long offerId);

  @Query("select o from TicketOffer o where o.publisher.club.id in :clubIds")
  List<TicketOffer> getOffersByClubIdIn(List<Long> clubIds);
}
