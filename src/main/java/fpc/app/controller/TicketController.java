package fpc.app.controller;

import fpc.app.dto.response.TicketOfferResponseDTO;
import fpc.app.dto.response.TicketTypeDTO;
import fpc.app.mapper.TicketMapper;
import fpc.app.model.app.TicketOffer;
import fpc.app.model.app.TicketType;
import fpc.app.model.auth.User;
import fpc.app.security.JwtUtil;
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

@RestController
@RequestMapping("/ticket")
@Tag(name = "Tickets")
@RequiredArgsConstructor
@Validated
public class TicketController {
  private final TicketService ticketService;
  private final UserService userService;
  private final JwtUtil jwtUtil;
  private final SubscriptionService subscriptionService;

  @GetMapping
  @Operation(summary = "Get all ticket offers available for sale")
  public ResponseEntity<List<TicketOfferResponseDTO>> getAvailableTicketOffers(
      @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
    User user = userService.getUser(jwtUtil.getUserId(token));
    List<Long> clubIds = subscriptionService.getSubscriptionsIds(user);
    List<TicketOffer> offers = ticketService.getActiveOffers(clubIds);
    return ResponseEntity.ok(TicketMapper.toResponseDTO(offers));
  }

  @GetMapping("/types")
  @Operation(summary = "Get ticket details")
  public ResponseEntity<List<TicketTypeDTO>> getTicketOfferTypes(
      @RequestParam @Positive Long ticketId) {
    List<TicketType> offerTypes = ticketService.getTicketOfferTypes(ticketId);
    return ResponseEntity.ok(TicketMapper.toTicketTypeDTO(offerTypes));
  }
}
