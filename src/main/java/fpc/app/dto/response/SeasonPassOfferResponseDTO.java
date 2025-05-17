package fpc.app.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record SeasonPassOfferResponseDTO(
    String description, Integer year, Integer season, LocalDateTime endDate, String imageId) {}
