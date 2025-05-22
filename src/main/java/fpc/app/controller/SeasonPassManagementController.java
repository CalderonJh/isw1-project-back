package fpc.app.controller;

import fpc.app.constant.OfferStatusType;
import fpc.app.dto.request.CreateSeasonPassOfferDTO;
import fpc.app.dto.request.StandPriceDTO;
import fpc.app.dto.response.SeasonPassOfferResponseDTO;
import fpc.app.dto.util.DateRange;
import fpc.app.mapper.SeasonPassOfferMapper;
import fpc.app.model.app.Club;
import fpc.app.model.app.SeasonPassOffer;
import fpc.app.security.JwtUtil;
import fpc.app.service.app.ClubService;
import fpc.app.service.app.SeasonPassOfferService;
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
@RequestMapping("/club-admin/season-pass")
@Tag(name = "Season Pass Management")
@RequiredArgsConstructor
public class SeasonPassManagementController {

  private final JwtUtil jwtUtil;
  private final SeasonPassOfferService seasonPassOfferService;
  private final ClubService clubService;

  @PostMapping(
      value = "/create",
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  @Operation(summary = "Create a season pass offer")
  public ResponseEntity<Void> create(
      @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
      @RequestPart("offer") CreateSeasonPassOfferDTO dto,
      @RequestPart("file") MultipartFile image) {
    Long userId = jwtUtil.getUserId(token);
    seasonPassOfferService.createSeasonPassOffer(userId, dto, image);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/all")
  @Operation(summary = "Get all season pass offers of the current club")
  public ResponseEntity<List<SeasonPassOfferResponseDTO>> getAll(
      @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
    Long userId = jwtUtil.getUserId(token);
    Club club = clubService.getClubByAdminId(userId);
    List<SeasonPassOffer> offers = seasonPassOfferService.getAllSeasonPassOffers(club);
    return ResponseEntity.ok(SeasonPassOfferMapper.toResponseDTO(offers));
  }

  @ApiResponse(
      responseCode = "200",
      description = "Nuevo estado de la oferta",
      content =
          @Content(
              mediaType = "application/json",
              examples = @ExampleObject(value = "{\"status\": \"ENABLED\"}")))
  @PutMapping("/{offerId}/toggle-status")
  @Operation(summary = "Toggle season pass offer status")
  @PreAuthorize("hasPermission(#offerId, 'SeasonPassOffer', 'ANY')")
  public ResponseEntity<Map<String, OfferStatusType>> toggleStatus(@PathVariable Long offerId) {
    OfferStatusType status = seasonPassOfferService.toggleSeasonPassOfferStatus(offerId);
    Map<String, OfferStatusType> map = Map.of("status", status);
    return ResponseEntity.ok(map);
  }

  @PutMapping(
      value = "/{id}/update/image",
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  @Operation(summary = "Update season pass offer image")
  @PreAuthorize("hasPermission(#id, 'SeasonPassOffer', 'ANY')")
  public ResponseEntity<Void> updateImage(
      @PathVariable Long id, @RequestPart("file") MultipartFile file) {
    seasonPassOfferService.updateSeasonPassOfferImage(id, file);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/{id}/update/dates")
  @Operation(summary = "Update season pass offer dates")
  @PreAuthorize("hasPermission(#id, 'SeasonPassOffer', 'ANY')")
  public ResponseEntity<Void> updateDate(@PathVariable Long id, @RequestBody DateRange dates) {
    seasonPassOfferService.updateDates(id, dates);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/{id}/update/price")
  @PreAuthorize("hasPermission(#id, 'SeasonPassOffer', 'ANY')")
  @Operation(summary = "Update ticket offer price")
  public ResponseEntity<Void> updateTicketOfferPrice(
      @PathVariable Long id, @RequestBody @Valid Set<StandPriceDTO> prices) {
    seasonPassOfferService.updateSeasonPassOfferPrice(id, prices);
    return ResponseEntity.ok().build();
  }
}
