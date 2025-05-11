package fpc.app.mapper;

import fpc.app.dto.app.TicketOfferDTO;
import fpc.app.model.app.TicketOffer;
import java.util.List;

public class TicketMapper {
  public static TicketOfferDTO toDTO(TicketOffer ticketOffer) {
    return TicketOfferDTO.builder()
        .id(ticketOffer.getId())
        .homeClub(ticketOffer.getMatch().getHomeClub().getShortName())
        .awayClub(ticketOffer.getMatch().getAwayClub().getShortName())
        .date(ticketOffer.getMatch().getStartDate())
        .imageId(ticketOffer.getImageId())
        .build();
  }

  public static List<TicketOfferDTO> toDTO(List<TicketOffer> offers) {
    return offers.stream().map(TicketMapper::toDTO).toList();
  }

  private TicketMapper() {}
}
