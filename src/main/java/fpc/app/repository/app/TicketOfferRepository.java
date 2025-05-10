package fpc.app.repository.app;

import fpc.app.model.app.TicketOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TicketOfferRepository extends JpaRepository<TicketOffer, Long> {
	@Query("select o.postedBy.club.id from TicketOffer o where o.id = :offerId")
	Long getClubId(Long offerId);
}
