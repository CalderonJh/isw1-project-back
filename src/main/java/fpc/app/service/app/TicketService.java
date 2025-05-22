package fpc.app.service.app;

import fpc.app.constant.OfferStatusType;
import fpc.app.dto.request.CreateTicketOfferDTO;
import fpc.app.dto.request.StandPriceDTO;
import fpc.app.dto.util.DateRange;
import fpc.app.model.app.Club;
import fpc.app.model.app.TicketOffer;
import fpc.app.model.app.TicketType;
import fpc.app.model.auth.User;
import java.util.List;
import java.util.Set;

import jakarta.validation.constraints.Positive;
import org.springframework.web.multipart.MultipartFile;

public interface TicketService {

	void createTicketOffer(Long publisherId, Long matchId, CreateTicketOfferDTO dto, MultipartFile file);

	OfferStatusType toggleTicketOfferStatus(Long ticketOfferId);

	List<TicketOffer> getActiveOffers(List<Long> clubIds, boolean getAll);

	List<TicketType> getTicketOfferTypes(@Positive Long ticketId);

	List<TicketOffer> getAllClubOffers(Club club);

	void updateTicketOfferImage(Long offerId, MultipartFile image);

	void updateTicketOfferDates(Long offerId, DateRange dateRange);

	void updateTicketOfferPrice(Long offerId, Set<StandPriceDTO> prices);

	void purchase(Long ticketTypeId, User buyer);
}
