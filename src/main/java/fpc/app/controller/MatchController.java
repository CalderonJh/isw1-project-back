package fpc.app.controller;

import static fpc.app.util.Tools.required;

import fpc.app.dto.app.MatchDTO;
import fpc.app.model.app.Club;
import fpc.app.model.app.Match;
import fpc.app.security.JwtUtil;
import fpc.app.service.app.ClubService;
import fpc.app.service.app.MatchService;
import io.swagger.v3.oas.annotations.Operation;
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
  public ResponseEntity<List<MatchDTO>> getAllMatches(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
    String username = jwtUtil.extractEmail(token);
    Club club = required(clubService.getClubByAdmin(username));
    var matches = matchService.getMatches(club);
    return ResponseEntity.ok(matches.stream().map(this::toDTO).toList());
  }

  @PostMapping("/save")
  @Operation(summary = "Create or update a match")
  public ResponseEntity<Void> saveMatch(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
      @RequestBody @Valid MatchDTO matchDTO) {
    String username = jwtUtil.extractEmail(token);

      matchService.create(username, matchDTO);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PutMapping("/update/{matchId}")
  @PreAuthorize("hasPermission(#matchId, 'Match', 'ANY')")
  @Operation(summary = "Update a match")
  public ResponseEntity<Void> updateMatch(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
      @PathVariable Long matchId,
      @RequestBody @Valid MatchDTO matchDTO) {
    String username = jwtUtil.extractEmail(token);

    if (matchDTO.matchId() == null) {
      matchService.create(username, matchDTO);
    } else {
      matchService.update(matchId, matchDTO);
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasPermission(#id, 'Match', 'ANY')")
  public ResponseEntity<MatchDTO> removeMatch(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable Long id) {
    String username = jwtUtil.extractEmail(token);
    matchService.deleteMatch(username, id);
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
