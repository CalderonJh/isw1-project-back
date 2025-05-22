package fpc.app.repository.app;

import fpc.app.model.app.TicketType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {
	List<TicketType> findByTicketOfferId(Long ticketOfferId);

	Optional<TicketType> findByTicketOfferIdAndStandId(Long offerId, @NotNull @Positive Long aLong);

  @Query(
      nativeQuery = true,
      value =
          "select v.available_ticket from app.ticket_type_purchases v where v.ticket_type_id = :ticketTypeId")
  Boolean isAvailable(Long ticketTypeId);
}
