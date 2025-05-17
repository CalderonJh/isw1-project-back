package fpc.app.service.app;

import fpc.app.dto.request.CreateSeasonPassOfferDTO;
import fpc.app.dto.response.SeasonPassOfferDetailDTO;
import fpc.app.model.app.ClubAdmin;
import fpc.app.model.app.SeasonPassOffer;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface SeasonPassOfferService {
  void createSeasonPassOffer(
      ClubAdmin clubAdmin, CreateSeasonPassOfferDTO dto, MultipartFile image);

  List<SeasonPassOffer> getSeasonPassOffers(List<Long> clubIds, boolean getAll);

  SeasonPassOfferDetailDTO getSeasonPassTypes(Long seasonPassOfferId);

  SeasonPassOffer getSeasonPassOffer(Long seasonPassOfferId);
}
