package fpc.app.dto.response;

import fpc.app.dto.util.DateRange;
import fpc.app.dto.util.Reference;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record TicketOfferResponseDTO(
    Long id,
    Reference homeClub,
    Reference awayClub,
    Reference stadium,
    LocalDateTime matchDay,
    DateRange offerPeriod,
    String imageId,
    boolean isPaused) {}
