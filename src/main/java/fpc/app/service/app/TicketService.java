package fpc.app.service.app;

import fpc.app.dto.app.CreateTicketOfferDTO;
import fpc.app.model.auth.User;
import org.springframework.web.multipart.MultipartFile;

public interface TicketService {
	void createTicketOffer();

	void createTicketOffer(User creator, CreateTicketOfferDTO dto, MultipartFile file);
}
