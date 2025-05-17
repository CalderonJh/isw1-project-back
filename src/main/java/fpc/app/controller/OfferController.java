package fpc.app.controller;

import fpc.app.dto.response.SeasonPassOfferDetailDTO;
import fpc.app.dto.response.SeasonPassOfferResponseDTO;
import fpc.app.dto.response.StandPricingDTO;
import fpc.app.dto.response.TicketOfferResponseDTO;
import fpc.app.mapper.SeasonPassOfferMapper;
import fpc.app.mapper.TicketMapper;
import fpc.app.model.app.SeasonPassOffer;
import fpc.app.model.app.TicketOffer;
import fpc.app.model.app.TicketType;
import fpc.app.model.auth.User;
import fpc.app.security.JwtUtil;
import fpc.app.service.app.SeasonPassOfferService;
import fpc.app.service.app.SubscriptionService;
import fpc.app.service.app.TicketService;
import fpc.app.service.auth.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/offer")
@Tag(name = "Tickets and Season Pass Offers")
public class OfferController {
  private final TicketService ticketService;
  private final UserService userService;
  private final JwtUtil jwtUtil;
  private final SubscriptionService subscriptionService;
  private final SeasonPassOfferService seasonPassOfferService;

  @GetMapping("/ticket")
  @Operation(summary = "Get all ticket offers available published by user subscribed clubs")
  @Parameter(
      name = "all",
      description = "If true, return all ticket offers, otherwise only those from subscribed clubs")
  public ResponseEntity<List<TicketOfferResponseDTO>> getAvailableTicketOffers(
      @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
      @RequestParam(value = "all", required = false, defaultValue = "false") boolean getAll) {
    User user = userService.getUser(jwtUtil.getUserId(token));
    List<Long> clubIds = subscriptionService.getSubscriptionsIds(user);
    List<TicketOffer> offers = ticketService.getActiveOffers(clubIds, getAll);
    return ResponseEntity.ok(TicketMapper.toResponseDTO(offers));
  }

  @GetMapping("/ticket/details/{ticketId}")
  @Operation(summary = "Get ticket prices by stand")
  public ResponseEntity<List<StandPricingDTO>> getTicketOfferTypes(
      @PathVariable @Positive Long ticketId) {
    List<TicketType> offerTypes = ticketService.getTicketOfferTypes(ticketId);
    return ResponseEntity.ok(TicketMapper.toTicketTypeDTO(offerTypes));
  }

  @GetMapping("/season-pass")
  @Operation(summary = "Get all season pass offers available for sale")
  @Parameter(
      name = "all",
      description = "If true, return all ticket offers, otherwise only those from subscribed clubs")
  public ResponseEntity<List<SeasonPassOfferResponseDTO>> getSeasonPassOffers(
      @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
      @RequestParam(value = "all", required = false, defaultValue = "false") boolean getAll) {
    User user = userService.getUser(jwtUtil.getUserId(token));
    List<Long> clubIds = subscriptionService.getSubscriptionsIds(user);
    List<SeasonPassOffer> offers = seasonPassOfferService.getSeasonPassOffers(clubIds, getAll);
    return ResponseEntity.ok(SeasonPassOfferMapper.toResponseDTO(offers));
  }

  @GetMapping("/season-pass/details/{seasonPassId}")
  @Operation(summary = "Get season pass prices by stand")
  public ResponseEntity<SeasonPassOfferDetailDTO> getSeasonPassOfferTypes(
      @PathVariable @Positive Long seasonPassId) {
    SeasonPassOfferDetailDTO offerTypes = seasonPassOfferService.getSeasonPassTypes(seasonPassId);
    return ResponseEntity.ok(offerTypes);
  }
}
