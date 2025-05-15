package fpc.app.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

public record CreateTicketOfferDTO(
    @Size(min = 1) List<@Valid StandPriceDTO> standPrices,
    @Nullable
        @Schema(
            description =
                "Sale start date, if it is not defined, a default one will be assigned (current time)")
        LocalDateTime saleStartDate,
    @Nullable
        @Schema(
            description =
                "Sale end date, if it is not defined, a default one will be assigned (match start time)")
        LocalDateTime saleEndDate) {}
