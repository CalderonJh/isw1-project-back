package fpc.app.mapper;

import fpc.app.dto.response.TicketOfferResponseDTO;
import fpc.app.dto.response.TicketTypeDTO;
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

  public static TicketTypeDTO toTicketTypeDTO(TicketType ticketType) {
    return new TicketTypeDTO(ticketType.getStand().getName(), ticketType.getPrice());
  }

  public static List<TicketTypeDTO> toTicketTypeDTO(List<TicketType> ticketTypes) {
    return ticketTypes.stream().map(TicketMapper::toTicketTypeDTO).toList();
  }

  private TicketMapper() {}
}
