package fpc.app.controller;

import fpc.app.dto.app.CreateTicketOfferDTO;
import fpc.app.model.auth.User;
import fpc.app.security.JwtUtil;
import fpc.app.service.app.TicketService;
import fpc.app.service.auth.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/club-admin/ticket")
@RequiredArgsConstructor
@Tag(name = "Tickets offer management")
public class AdminTicketController {
  private final TicketService ticketService;
  private final UserService userService;
  private final JwtUtil jwtUtil;

  @PostMapping(
      value = "/create",
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  @PreAuthorize("hasPermission(#matchId, 'Match', 'ANY')")
  public ResponseEntity<Void> createTicketOffer(
      @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
      @RequestParam Long matchId,
      @RequestPart("offer") @Valid CreateTicketOfferDTO dto,
      @RequestPart("file") MultipartFile file) {

    User publisher = userService.getUser(jwtUtil.getUserId(token));
    ticketService.createTicketOffer(publisher, matchId, dto, file);
    return ResponseEntity.ok().build();
  }
}
