package fpc.app.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record SeasonPassOfferDetailDTO(List<StandPricingDTO> prices, List<String> games) {}
