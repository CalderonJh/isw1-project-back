package fpc.app.controller;

import fpc.app.dto.request.CreateSeasonPassOfferDTO;
import fpc.app.model.app.ClubAdmin;
import fpc.app.model.auth.User;
import fpc.app.security.JwtUtil;
import fpc.app.service.app.ClubAdminService;
import fpc.app.service.app.SeasonPassOfferService;
import fpc.app.service.auth.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/club-admin/season-pass")
@Tag(name = "Season Pass Management")
public class SeasonPassManagementController {

  private final UserService userService;
  private final JwtUtil jwtUtil;
  private final ClubAdminService clubAdminService;
  private final SeasonPassOfferService seasonPassOfferService;

  public SeasonPassManagementController(
      UserService userService,
      JwtUtil jwtUtil,
      ClubAdminService clubAdminService,
      SeasonPassOfferService seasonPassOfferService) {
    this.userService = userService;
    this.jwtUtil = jwtUtil;
    this.clubAdminService = clubAdminService;
    this.seasonPassOfferService = seasonPassOfferService;
  }

  @PostMapping(
      value = "/create",
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  @Operation(summary = "Create a season pass offer")
  public ResponseEntity<Void> create(
      @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
      @RequestPart("offer") CreateSeasonPassOfferDTO dto,
      @RequestPart("file") MultipartFile image) {
    User user = userService.getUser(jwtUtil.getUserId(token));
    ClubAdmin clubAdmin = clubAdminService.getClubAdmin(user);
    seasonPassOfferService.createSeasonPassOffer(clubAdmin, dto, image);
    return ResponseEntity.ok().build();
  }
}
