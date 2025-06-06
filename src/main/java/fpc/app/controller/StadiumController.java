package fpc.app.controller;

import fpc.app.dto.StadiumDTO;
import fpc.app.mapper.StadiumMapper;
import fpc.app.model.app.Club;
import fpc.app.model.app.Stadium;
import fpc.app.security.JwtUtil;
import fpc.app.service.app.ClubService;
import fpc.app.service.app.StadiumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
  private final ClubService clubService;

  @PostMapping(
      value = "/create",
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  @Operation(summary = "Create a new stadium")
  public ResponseEntity<Void> createStadium(
      @RequestPart("stadium") @Valid StadiumDTO request,
      @RequestPart("image") MultipartFile image,
      @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
    Long userId = jwtUtil.getUserId(token);
    Club club = clubService.getClubByAdminId(userId);
    stadiumService.createStadium(club, request, image);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/update/{id}")
  @PreAuthorize("hasPermission(#id, 'Stadium', 'ANY')")
  @Operation(summary = "Update stadium details")
  public ResponseEntity<Void> updateStadium(
      @PathVariable Long id, @RequestBody @Valid StadiumDTO request) {
    stadiumService.updateStadium(id, request);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/update/{id}/image")
  @PreAuthorize("hasPermission(#id, 'Stadium', 'ANY')")
  @Operation(summary = "Update stadium image")
  public ResponseEntity<Void> updateStadiumImage(
      @PathVariable Long id, @RequestPart("image") MultipartFile image) {
    stadiumService.updateStadiumImage(id, image);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/all")
  @Operation(summary = "Get all stadiums of the current club")
  public ResponseEntity<List<StadiumDTO>> getAllStadiums(
      @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
    Long userId = jwtUtil.getUserId(token);
    Club club = clubService.getClubByAdminId(userId);
    List<Stadium> stadiums = stadiumService.getStadiums(club);
    return ResponseEntity.ok(StadiumMapper.map(stadiums));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a stadium")
  @PreAuthorize("hasPermission(#id, 'Stadium', 'ANY')")
  public ResponseEntity<StadiumDTO> getStadium(@PathVariable Long id) {
    Stadium stadium = stadiumService.getStadium(id);
    return ResponseEntity.ok(StadiumMapper.map(stadium));
  }

  @DeleteMapping("/delete/{id}")
  @Operation(summary = "Delete a stadium")
  @PreAuthorize("hasPermission(#id, 'Stadium', 'ANY')")
  public ResponseEntity<Void> deleteStadium(@PathVariable Long id) {
    stadiumService.deleteStadium(id);
    return ResponseEntity.ok().build();
  }
}
