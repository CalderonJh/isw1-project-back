package fpc.app.controller;

import fpc.app.dto.app.ClubCreateDTO;
import fpc.app.dto.app.ClubDTO;
import fpc.app.service.app.ClubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/su/club")
@RequiredArgsConstructor
@Tag(name = "Club", description = "Endpoints for managing clubs")
public class ClubController {
  private final ClubService clubService;

  @PostMapping("/create")
  public ResponseEntity<Void> create(
      @RequestPart(value = "image", required = false) MultipartFile file,
      @RequestPart("club") ClubCreateDTO request) {
    clubService.createClub(request, file);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/update/{clubId}")
  public ResponseEntity<Void> update(
      @PathVariable Long clubId,
      @RequestPart(value = "file", required = false) MultipartFile file,
      @RequestPart("club") ClubCreateDTO request) {
    clubService.update(clubId, request, file);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/list")
  @Operation(summary = "List all clubs")
  public ResponseEntity<List<ClubDTO>> list() {
    List<ClubDTO> clubs = clubService.list();
    return ResponseEntity.ok(clubs);
  }
}
