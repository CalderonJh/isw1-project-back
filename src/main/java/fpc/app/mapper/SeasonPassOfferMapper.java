package fpc.app.mapper;

import fpc.app.dto.response.SeasonPassOfferDetailDTO;
import fpc.app.dto.response.SeasonPassOfferResponseDTO;
import fpc.app.dto.response.StandPricingDTO;
import fpc.app.model.app.Match;
import fpc.app.model.app.SeasonPassOffer;
import fpc.app.model.app.SeasonPassType;
import java.util.List;

public class SeasonPassOfferMapper {
  private SeasonPassOfferMapper() {}

  public static SeasonPassOfferResponseDTO toResponseDTO(SeasonPassOffer offer) {
    return SeasonPassOfferResponseDTO.builder()
        .description(offer.getDescription())
        .year(offer.getYear())
        .season(offer.getSeason())
        .endDate(offer.getEndDate())
        .imageId(offer.getImageId())
        .build();
  }

  public static List<SeasonPassOfferResponseDTO> toResponseDTO(List<SeasonPassOffer> offers) {
    return offers.stream().map(SeasonPassOfferMapper::toResponseDTO).toList();
  }

  public static SeasonPassOfferDetailDTO toDetailDTO(
      List<SeasonPassType> types, SeasonPassOffer offer) {
    return SeasonPassOfferDetailDTO.builder()
        .prices(types.stream().map(SeasonPassOfferMapper::mapToStandPricingDTO).toList())
        .games(offer.getMatches().stream().map(SeasonPassOfferMapper::getAwayTeamName).toList())
        .build();
  }

  private static StandPricingDTO mapToStandPricingDTO(SeasonPassType passType) {
    return new StandPricingDTO(
        passType.getId(),
        passType.getStand().getName(),
        passType.getPrice(),
        passType.isAvailable());
  }

  private static String getAwayTeamName(Match match) {
    return match.getAwayClub().getShortName();
  }
}
