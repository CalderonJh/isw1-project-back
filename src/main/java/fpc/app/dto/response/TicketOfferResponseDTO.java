package fpc.app.dto.response;

import java.time.LocalDateTime;

import fpc.app.dto.util.Suggestion;
import lombok.Builder;

@Builder
public record TicketOfferResponseDTO(
    Long id,
    Suggestion homeClub,
    Suggestion awayClub,
    LocalDateTime date,
    String imageId,
    boolean isPaused) {}
