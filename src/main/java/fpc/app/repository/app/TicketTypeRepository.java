package fpc.app.repository.app;

import fpc.app.model.app.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {
	List<TicketType> findByTicketOfferId(Long ticketOfferId);
}
