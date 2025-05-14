package fpc.app.controller;

import static fpc.app.util.Tools.hasText;

import fpc.app.constant.MatchSearchType;
import fpc.app.dto.request.MatchCreationDTO;
import fpc.app.dto.response.ClubResponseDTO;
import fpc.app.dto.response.MatchResponseDTO;
import fpc.app.mapper.ClubMapper;
import fpc.app.mapper.MatchMapper;
import fpc.app.model.app.Club;
import fpc.app.model.auth.User;
import fpc.app.security.JwtUtil;
import fpc.app.service.app.ClubAdminService;
import fpc.app.service.app.ClubService;
import fpc.app.service.app.MatchService;
import fpc.app.service.auth.UserService;
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
  private final UserService userService;
  private final ClubAdminService clubAdminService;

  @GetMapping("/all")
  @Parameter(
      name = "searchType",
      description =
          "Used to get only valid matches for ticket or season pass offers creation <br> T: Ticket offer <br> S: Season pass offer")
  @Operation(summary = "List matches")
  public ResponseEntity<List<MatchResponseDTO>> getAllMatches(
      @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
      @RequestParam(required = false) String searchType) {
    MatchSearchType matchSearchType =
        hasText(searchType) ? MatchSearchType.fromString(searchType) : MatchSearchType.ALL;
    Long userId = jwtUtil.getUserId(token);
    Club club = clubService.getClubByAdmin(userId);
    var matches = matchService.getMatches(club, matchSearchType);
    return ResponseEntity.ok(MatchMapper.toResponseDTO(matches));
  }

  @GetMapping("/clubs")
  @Operation(summary = "List available clubs for match creation, that is all except the current club")
  public ResponseEntity<List<ClubResponseDTO>> list(
      @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
    User user = userService.getUser(jwtUtil.getUserId(token));
    Club club = clubService.getClubByAdmin(user);
    List<Club> clubs = clubService.listForMatch(club);
    return ResponseEntity.ok(ClubMapper.map(clubs));
  }

  @PostMapping("/save")
  @Operation(summary = "Create a match")
  public ResponseEntity<Void> saveMatch(
      @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
      @RequestBody @Valid MatchCreationDTO matchCreationDTO) {
    Long userId = jwtUtil.getUserId(token);
    User user = userService.getUser(userId);
    Club homeClub = clubAdminService.getClub(user);

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
  public ResponseEntity<MatchCreationDTO> removeMatch(@PathVariable Long id) {
    matchService.deleteMatch(id);
    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
