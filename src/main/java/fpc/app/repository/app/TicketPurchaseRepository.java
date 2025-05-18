package fpc.app.repository.app;

import fpc.app.model.app.TicketPurchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketPurchaseRepository extends JpaRepository<TicketPurchase, Long> {}
