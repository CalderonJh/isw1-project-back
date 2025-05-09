package fpc.app.dto.app;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record StandPriceDTO(
    @NotNull @Positive Long standId, @NotNull @Positive BigDecimal price, boolean isDisabled) {}
