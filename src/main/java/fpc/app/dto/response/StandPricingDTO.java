package fpc.app.dto.response;

import fpc.app.dto.util.Reference;
import java.math.BigDecimal;

public record StandPricingDTO(Long saleId, Reference stand, BigDecimal price, boolean available) {}
