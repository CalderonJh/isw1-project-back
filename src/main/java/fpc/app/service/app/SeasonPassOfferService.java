package fpc.app.service.app;

import fpc.app.constant.OfferStatus;
import fpc.app.dto.request.CreateSeasonPassOfferDTO;
import fpc.app.dto.request.StandPriceDTO;
import fpc.app.dto.response.SeasonPassOfferDetailDTO;
import fpc.app.dto.util.DateRange;
import fpc.app.model.app.Club;
import fpc.app.model.app.ClubAdmin;
import fpc.app.model.app.SeasonPassOffer;
import fpc.app.model.auth.User;
import java.util.List;
import java.util.Set;
import org.springframework.web.multipart.MultipartFile;

public interface SeasonPassOfferService {
  void createSeasonPassOffer(
      ClubAdmin clubAdmin, CreateSeasonPassOfferDTO dto, MultipartFile image);

  List<SeasonPassOffer> getSeasonPassOffers(List<Long> clubIds, boolean getAll);

  SeasonPassOfferDetailDTO getSeasonPassTypes(Long seasonPassOfferId);

  SeasonPassOffer getSeasonPassOffer(Long seasonPassOfferId);

	void purchase(Long seasonPassTypeId, User buyer);

  List<SeasonPassOffer> getAllSeasonPassOffers(Club club);

  OfferStatus toggleSeasonPassOfferStatus(Long offerId);

  void updateSeasonPassOfferImage(Long id, MultipartFile file);

  void updateDates(Long offerId, DateRange dateRange);

  void updateSeasonPassOfferPrice(Long id, Set<StandPriceDTO> prices);
}
