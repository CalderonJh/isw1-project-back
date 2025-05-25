package fpc.app.dto.response;

import fpc.app.dto.util.Reference;
import java.util.List;
import lombok.Builder;

@Builder
public record SeasonPassOfferDetailDTO(
    List<StandPricingDTO> prices, List<String> games, Reference stadium, String stadiumImageId) {}
