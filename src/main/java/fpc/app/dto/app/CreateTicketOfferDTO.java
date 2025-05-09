package fpc.app.dto.app;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

public record CreateTicketOfferDTO(
    @NotNull @Positive Long matchId,
    @Size(min = 1) List<@Valid StandPriceDTO> standPrices,
    @Nullable LocalDateTime saleStartDate,
    @Nullable LocalDateTime saleEndDate) {}
