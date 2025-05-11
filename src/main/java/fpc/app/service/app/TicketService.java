package fpc.app.service.app;

import fpc.app.dto.app.CreateTicketOfferDTO;
import fpc.app.model.app.TicketOffer;
import fpc.app.model.auth.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TicketService {

	void createTicketOffer(User creator, Long matchId, CreateTicketOfferDTO dto, MultipartFile file);

	List<TicketOffer> getOffers(List<Long> clubIds);
}
