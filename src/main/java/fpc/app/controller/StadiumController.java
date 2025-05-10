package fpc.app.controller;

import fpc.app.dto.app.StadiumDTO;
import fpc.app.dto.app.StandDTO;
import fpc.app.model.app.Club;
import fpc.app.model.app.Stadium;
import fpc.app.model.app.Stand;
import fpc.app.model.auth.User;
import fpc.app.security.JwtUtil;
import fpc.app.service.app.ClubAdminService;
import fpc.app.service.app.StadiumService;
import fpc.app.service.auth.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/club-admin/stadium")
@RequiredArgsConstructor
@Tag(name = "Stadium", description = "Stadium management")
public class StadiumController {
  private final StadiumService stadiumService;
  private final JwtUtil jwtUtil;
  private final UserService userService;
  private final ClubAdminService clubAdminService;

  @PostMapping("/create")
  @Operation(summary = "Create a new stadium")
  public ResponseEntity<Void> createStadium(
      @RequestPart("stadium") @Valid StadiumDTO request,
      @RequestPart("image") MultipartFile image,
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
    Long userId = jwtUtil.getUserId(token);
    User creator = userService.getUser(userId);
    Club club = clubAdminService.getClub(creator);
    stadiumService.createStadium(club, request, image);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/update/{id}")
  @PreAuthorize("hasPermission(#id, 'Stadium', 'ANY')")
  public ResponseEntity<Void> updateStadium(
      @PathVariable Long id, @RequestPart("stadium") @Valid StadiumDTO request) {
    stadiumService.updateStadium(id, request);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/all")
  @Operation(summary = "Get all stadiums")
  @PreAuthorize("hasPermission(#clubId, 'Club', 'ANY')")
  public ResponseEntity<List<StadiumDTO>> getAllStadiums(@RequestParam Long clubId) {
    List<Stadium> stadiums = stadiumService.getStadiums(clubId);
    return ResponseEntity.ok(stadiums.stream().map(this::mapStadiumDTO).toList());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a stadium")
  @PreAuthorize("hasPermission(#id, 'Stadium', 'ANY')")
  public ResponseEntity<StadiumDTO> getStadium(@PathVariable Long id) {
    Stadium stadium = stadiumService.getStadium(id);
    return ResponseEntity.ok(mapStadiumDTO(stadium));
  }

  @DeleteMapping("/delete/{id}")
  @Operation(summary = "Delete a stadium")
  @PreAuthorize("hasPermission(#id, 'Stadium', 'ANY')")
  public ResponseEntity<Void> deleteStadium(@PathVariable Long id) {
    stadiumService.deleteStadium(id);
    return ResponseEntity.ok().build();
  }

  private StadiumDTO mapStadiumDTO(Stadium stadium) {
    if (stadium == null) return null;
    return new StadiumDTO(
        stadium.getId(),
        stadium.getName(),
        stadium.getStands().stream().map(this::mapStandDTO).toList());
  }

  private StandDTO mapStandDTO(Stand stand) {
    return new StandDTO(stand.getName(), stand.getCapacity());
  }
}
