package fpc.app.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

public record CreateSeasonPassOfferDTO(
	String description,
    Integer year,
    Integer season,
    @Size(min = 1) Set<Long> matchIds,
    @Size(min = 1) Set<StandPriceDTO> standPrices,
    @Nullable LocalDateTime startDate,
    @Nullable LocalDateTime endDate) {}
