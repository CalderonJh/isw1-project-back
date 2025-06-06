package fpc.app.controller;

import fpc.app.dto.UserDTO;
import fpc.app.dto.request.ClubCreateDTO;
import fpc.app.dto.response.ClubResponseDTO;
import fpc.app.mapper.ClubMapper;
import fpc.app.mapper.UserMapper;
import fpc.app.model.app.Club;
import fpc.app.service.app.ClubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/su/club")
@RequiredArgsConstructor
@Tag(name = "Club", description = "Endpoints for managing clubs")
public class ClubController {
  private final ClubService clubService;

  @GetMapping("/{id}/admin")
  @Operation(summary = "Get user who manages the club")
  public ResponseEntity<UserDTO> getClubAdmin(@PathVariable Long id) {
    Club club = clubService.getClubById(id);
    return ResponseEntity.ok(UserMapper.map(club.getAdmin()));
  }

  @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "Create a club with logo")
  public ResponseEntity<Void> create(
      @RequestPart(value = "image", required = false) MultipartFile file,
      @RequestPart("club") @Valid ClubCreateDTO request) {
    clubService.createClub(request, file);
    return ResponseEntity.ok().build();
  }

  @PutMapping(
      value = "/update/{clubId}",
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  @Operation(summary = "Update club name or image")
  public ResponseEntity<Void> update(
      @PathVariable Long clubId,
      @RequestPart(value = "file", required = false) MultipartFile file,
      @RequestPart("club") @Valid ClubCreateDTO request) {
    clubService.update(clubId, request, file);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/list")
  @Operation(summary = "List all clubs")
  public ResponseEntity<List<ClubResponseDTO>> list() {
    List<Club> clubs = clubService.list();
    return ResponseEntity.ok(ClubMapper.map(clubs, true));
  }
}
