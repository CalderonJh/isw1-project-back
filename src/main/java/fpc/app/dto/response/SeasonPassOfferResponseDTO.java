package fpc.app.dto.response;

import fpc.app.dto.util.DateRange;
import lombok.Builder;

@Builder
public record SeasonPassOfferResponseDTO(
    Long id,
    String description,
    Integer year,
    Integer season,
    DateRange offerPeriod,
    String imageId,
    boolean isPaused) {}
