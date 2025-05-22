package fpc.app.controller;

import static fpc.app.util.Tools.hasText;

import fpc.app.constant.MatchSearchType;
import fpc.app.dto.request.MatchCreationDTO;
import fpc.app.dto.response.ClubResponseDTO;
import fpc.app.dto.response.MatchResponseDTO;
import fpc.app.mapper.ClubMapper;
import fpc.app.mapper.MatchMapper;
import fpc.app.model.app.Club;
import fpc.app.security.JwtUtil;
import fpc.app.service.app.ClubService;
import fpc.app.service.app.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/club-admin/match")
@RequiredArgsConstructor
@Tag(name = "Match", description = "Match management")
public class MatchController {
  private final JwtUtil jwtUtil;
  private final MatchService matchService;
  private final ClubService clubService;

  @GetMapping("/all")
  @Parameter(
      name = "toOffer",
      description =
          "Used to get only valid matches for ticket or season pass offers creation, use: <br> toOffer=ticket for ticket offers <br> toOffer=season for season pass offers")
  @Operation(summary = "List matches")
  @Parameter(name = "stadium", description = "Stadium id. Allows filtering matches by stadium")
  public ResponseEntity<List<MatchResponseDTO>> getAllMatches(
      @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
      @RequestParam(name = "stadium", required = false) Long stadiumId,
      @RequestParam(required = false) String toOffer) {
    MatchSearchType matchSearchType =
        hasText(toOffer) ? MatchSearchType.fromString(toOffer) : MatchSearchType.ALL;
    Long userId = jwtUtil.getUserId(token);
    Club club = clubService.getClubByAdminId(userId);
    var matches = matchService.getMatches(club, matchSearchType, stadiumId);
    return ResponseEntity.ok(MatchMapper.toResponseDTO(matches));
  }

  @GetMapping("/clubs")
  @Operation(summary = "List available clubs for match creation, that is all except the current club")
  public ResponseEntity<List<ClubResponseDTO>> list(
      @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
    Long userId = jwtUtil.getUserId(token);
    Club club = clubService.getClubByAdminId(userId);
    List<Club> clubs = clubService.listForMatch(club);
    return ResponseEntity.ok(ClubMapper.map(clubs, false));
  }

  @PostMapping("/save")
  @Operation(summary = "Create a match")
  public ResponseEntity<Void> saveMatch(
      @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
      @RequestBody @Valid MatchCreationDTO matchCreationDTO) {
    Long userId = jwtUtil.getUserId(token);
    Club homeClub = clubService.getClubByAdminId(userId);

    matchService.create(homeClub, matchCreationDTO);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PutMapping("/update/{matchId}")
  @PreAuthorize("hasPermission(#matchId, 'Match', 'ANY')")
  @Operation(summary = "Update a match")
  public ResponseEntity<Void> updateMatch(
      @PathVariable Long matchId, @RequestBody @Valid MatchCreationDTO matchCreationDTO) {
    matchService.update(matchId, matchCreationDTO);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasPermission(#id, 'Match', 'ANY')")
  @Operation(summary = "Delete a match")
  public ResponseEntity<Void> removeMatch(@PathVariable Long id) {
    matchService.deleteMatch(id);
    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
