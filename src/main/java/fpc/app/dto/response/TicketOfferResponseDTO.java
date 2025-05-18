package fpc.app.dto.response;

import java.time.LocalDateTime;

import fpc.app.dto.util.DateRange;
import fpc.app.dto.util.Suggestion;
import lombok.Builder;

@Builder
public record TicketOfferResponseDTO(
    Long id,
    Suggestion homeClub,
    Suggestion awayClub,
    LocalDateTime matchDay,
		DateRange offerPeriod,
    String imageId,
    boolean isPaused) {}
