package fpc.app.controller;

import fpc.app.constant.OfferStatusType;
import fpc.app.dto.request.CreateTicketOfferDTO;
import fpc.app.dto.request.StandPriceDTO;
import fpc.app.dto.response.TicketOfferResponseDTO;
import fpc.app.dto.util.DateRange;
import fpc.app.mapper.TicketMapper;
import fpc.app.model.app.Club;
import fpc.app.model.app.TicketOffer;
import fpc.app.security.JwtUtil;
import fpc.app.service.app.ClubService;
import fpc.app.service.app.TicketService;
import fpc.app.service.auth.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
public class TicketManagementController {
  private final TicketService ticketService;
  private final JwtUtil jwtUtil;
  private final ClubService clubService;

  @PostMapping(
      value = "/create",
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  @PreAuthorize("hasPermission(#matchId, 'Match', 'ANY')")
  @Operation(summary = "Create a ticket sale offer for a match")
  public ResponseEntity<Void> createTicketOffer(
      @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
      @RequestParam Long matchId,
      @RequestPart("offer") @Valid CreateTicketOfferDTO dto,
      @RequestPart("file") MultipartFile file) {

    Long userId = jwtUtil.getUserId(token);
    ticketService.createTicketOffer(userId, matchId, dto, file);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/all")
  @Operation(summary = "Get all ticket offers for the current club")
  public ResponseEntity<List<TicketOfferResponseDTO>> getAllTicketOffers(
      @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
    Long userId = jwtUtil.getUserId(token);
    Club club = clubService.getClubByAdminId(userId);
    List<TicketOffer> offers = ticketService.getAllClubOffers(club);
    return ResponseEntity.ok(TicketMapper.toResponseDTO(offers));
  }

  @PatchMapping("/{id}/toggle-status")
  @ApiResponse(
      responseCode = "200",
      description = "Nuevo estado de la oferta",
      content =
          @Content(
              mediaType = "application/json",
              examples = @ExampleObject(value = "{\"status\": \"ENABLED\"}")))
  @PreAuthorize("hasPermission(#id, 'TicketOffer', 'ANY')")
  @Operation(summary = "Toggle ticket offer status")
  public ResponseEntity<Map<String, OfferStatusType>> changeTicketOfferStatus(
      @PathVariable Long id) {
    OfferStatusType status = ticketService.toggleTicketOfferStatus(id);
    Map<String, OfferStatusType> res = Map.of("status", status);
    return ResponseEntity.ok(res);
  }

  @PatchMapping(
      value = "/{id}/update/image",
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  @PreAuthorize("hasPermission(#id, 'TicketOffer', 'ANY')")
  @Operation(summary = "Update ticket offer image")
  public ResponseEntity<Void> updateTicketOfferImage(
      @PathVariable Long id, @RequestPart MultipartFile file) {
    ticketService.updateTicketOfferImage(id, file);
    return ResponseEntity.ok().build();
  }

  @PatchMapping("/{id}/update/dates")
  @PreAuthorize("hasPermission(#id, 'TicketOffer', 'ANY')")
  @Operation(summary = "Update ticket offer dates")
  public ResponseEntity<Void> updateTicketOfferDates(
      @PathVariable Long id, @RequestBody DateRange dateRange) {
    ticketService.updateTicketOfferDates(id, dateRange);
    return ResponseEntity.ok().build();
  }

  @PatchMapping("/{id}/update/price")
  @PreAuthorize("hasPermission(#id, 'TicketOffer', 'ANY')")
  @Operation(summary = "Update ticket offer price")
  public ResponseEntity<Void> updateTicketOfferPrice(
      @PathVariable Long id, @RequestBody @Valid Set<StandPriceDTO> prices) {
    ticketService.updateTicketOfferPrice(id, prices);
    return ResponseEntity.ok().build();
  }

}
