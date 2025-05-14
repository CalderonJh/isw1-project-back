package fpc.app.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

public record CreateTicketOfferDTO(
    @Size(min = 1) List<@Valid StandPriceDTO> standPrices,
    @Nullable LocalDateTime saleStartDate,
    @Nullable LocalDateTime saleEndDate) {}
