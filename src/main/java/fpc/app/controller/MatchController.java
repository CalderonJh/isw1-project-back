package fpc.app.controller;

import static fpc.app.util.Tools.required;

import fpc.app.dto.app.MatchDTO;
import fpc.app.model.app.Club;
import fpc.app.model.app.Match;
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
  public ResponseEntity<List<MatchDTO>> getAllMatches(
      @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
    Long userId = jwtUtil.getUserId(token);
    Club club = clubService.getClubByAdmin(userId);
    var matches = matchService.getMatches(club);
    return ResponseEntity.ok(matches.stream().map(this::toDTO).toList());
  }

  @PostMapping("/save")
  @Operation(summary = "Create or update a match")
  public ResponseEntity<Void> saveMatch(
      @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
      @RequestBody @Valid MatchDTO matchDTO) {
    Long userId = jwtUtil.getUserId(token);
    User user = required(userService.getUser(userId));
    Club homeClub = clubAdminService.getClub(user);

    matchService.create(homeClub, matchDTO);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PutMapping("/update/{matchId}")
  @PreAuthorize("hasPermission(#matchId, 'Match', 'ANY')")
  @Operation(summary = "Update a match")
  public ResponseEntity<Void> updateMatch(
      @PathVariable Long matchId, @RequestBody @Valid MatchDTO matchDTO) {
    matchService.update(matchId, matchDTO);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasPermission(#id, 'Match', 'ANY')")
  public ResponseEntity<MatchDTO> removeMatch(@PathVariable Long id) {
    matchService.deleteMatch(id);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  private MatchDTO toDTO(Match match) {
    return MatchDTO.builder()
        .matchId(match.getId())
        .awayClubId(match.getAwayClub().getId())
        .year(match.getYear())
        .season(match.getSeason())
        .stadiumId(match.getStadium().getId())
        .build();
  }
}
