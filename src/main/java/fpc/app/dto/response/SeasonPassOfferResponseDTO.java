package fpc.app.dto.response;

import fpc.app.dto.util.DateRange;
import fpc.app.dto.util.Reference;
import lombok.Builder;

@Builder
public record SeasonPassOfferResponseDTO(
    Long id,
    String description,
    Integer year,
    Integer season,
    DateRange offerPeriod,
    Reference stadium,
    String imageId,
    boolean isPaused) {}
