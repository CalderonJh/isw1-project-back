package fpc.app.dto.response;

import java.math.BigDecimal;

public record StandPricingDTO(Long saleId, String standName, BigDecimal price, boolean available) {}
