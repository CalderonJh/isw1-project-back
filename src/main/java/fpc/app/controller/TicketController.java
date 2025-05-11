package fpc.app.controller;

import fpc.app.dto.app.TicketOfferDTO;
import fpc.app.mapper.TicketMapper;
import fpc.app.model.app.TicketOffer;
import fpc.app.model.auth.User;
import fpc.app.security.JwtUtil;
import fpc.app.service.app.SubscriptionService;
import fpc.app.service.app.TicketService;
import fpc.app.service.auth.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ticket")
@Tag(name = "Tickets")
@RequiredArgsConstructor
public class TicketController {
  private final TicketService ticketService;
  private final UserService userService;
  private final JwtUtil jwtUtil;
  private final SubscriptionService subscriptionService;

  @GetMapping
  public ResponseEntity<List<TicketOfferDTO>> getAvailableTicketOffers(
      @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
    User user = userService.getUser(jwtUtil.getUserId(token));
    List<Long> clubIds = subscriptionService.getSubscriptionsIds(user);
    List<TicketOffer> offers = ticketService.getOffers(clubIds);
    return ResponseEntity.ok(TicketMapper.toDTO(offers));
  }
}
