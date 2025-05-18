package fpc.app.mapper;

import fpc.app.dto.response.TicketOfferResponseDTO;
import fpc.app.dto.response.StandPricingDTO;
import fpc.app.dto.util.Suggestion;
import fpc.app.model.app.Club;
import fpc.app.model.app.TicketOffer;
import fpc.app.model.app.TicketType;
import java.util.List;

public class TicketMapper {
  public static TicketOfferResponseDTO toResponseDTO(TicketOffer ticketOffer) {
    Club homeClub = ticketOffer.getMatch().getHomeClub();
    Club awayClub = ticketOffer.getMatch().getAwayClub();
    return TicketOfferResponseDTO.builder()
        .id(ticketOffer.getId())
        .homeClub(new Suggestion(homeClub.getId(), homeClub.getShortName()))
        .awayClub(new Suggestion(awayClub.getId(), awayClub.getShortName()))
        .date(ticketOffer.getMatch().getStartDate())
        .imageId(ticketOffer.getImageId())
        .isPaused(ticketOffer.isPaused())
        .build();
  }

  public static List<TicketOfferResponseDTO> toResponseDTO(List<TicketOffer> offers) {
    return offers.stream().map(TicketMapper::toResponseDTO).toList();
  }

  public static StandPricingDTO toTicketTypeDTO(TicketType ticketType) {
    return new StandPricingDTO(ticketType.getStand().getName(), ticketType.getPrice(), ticketType.isAvailable());
  }

  public static List<StandPricingDTO> toTicketTypeDTO(List<TicketType> ticketTypes) {
    return ticketTypes.stream().map(TicketMapper::toTicketTypeDTO).toList();
  }

  private TicketMapper() {}
}
