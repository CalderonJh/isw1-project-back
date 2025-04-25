package fpc.app.controller;

import fpc.app.dto.app.ClubRequest;
import fpc.app.service.app.ClubService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
      @RequestPart(value = "file", required = false) MultipartFile file,
      @RequestPart("club") ClubRequest request) {
    clubService.createClub(request, file);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/update/{clubId}")
  public ResponseEntity<Void> update(
      @PathVariable Long clubId,
      @RequestPart(value = "file", required = false) MultipartFile file,
      @RequestPart("club") ClubRequest request) {
    clubService.update(clubId, request, file);
    return ResponseEntity.ok().build();
  }

}
