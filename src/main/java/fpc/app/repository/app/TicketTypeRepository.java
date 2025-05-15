package fpc.app.repository.app;

import fpc.app.model.app.TicketType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {
	List<TicketType> findByTicketOfferId(Long ticketOfferId);

	TicketType findByTicketOfferIdAndStandId(Long offerId, @NotNull @Positive Long aLong);
}
