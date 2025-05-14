package fpc.app.service.app;

import fpc.app.constant.OfferStatus;
import fpc.app.dto.request.CreateTicketOfferDTO;
import fpc.app.model.app.TicketOffer;
import fpc.app.model.app.TicketType;
import fpc.app.model.auth.User;
import java.util.List;

import jakarta.validation.constraints.Positive;
import org.springframework.web.multipart.MultipartFile;

public interface TicketService {

	void createTicketOffer(User creator, Long matchId, CreateTicketOfferDTO dto, MultipartFile file);

	OfferStatus toggleTicketOfferStatus(Long ticketOfferId);

	List<TicketOffer> getActiveOffers(List<Long> clubIds);

	List<TicketType> getTicketOfferTypes(@Positive Long ticketId);
}
