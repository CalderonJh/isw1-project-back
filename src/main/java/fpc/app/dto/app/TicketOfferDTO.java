package fpc.app.dto.app;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record TicketOfferDTO(
    Long id, String homeClub, String awayClub, LocalDateTime date, String imageId) {}
