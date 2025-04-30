package fpc.app.controller;

import fpc.app.dto.app.StadiumDTO;
import fpc.app.mapper.StadiumMapper;
import fpc.app.model.app.Stadium;
import fpc.app.security.JwtUtil;
import fpc.app.service.app.StadiumService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/club-admin/stadium")
@RequiredArgsConstructor
public class StadiumController {
  private final StadiumService stadiumService;
  private final JwtUtil jwtUtil;
  private static final StadiumMapper MAPPER = StadiumMapper.INSTANCE;

  @PostMapping("/create")
  @Operation(summary = "Create a new stadium")
  public ResponseEntity<Void> createStadium(
      @RequestPart("stadium") @Valid StadiumDTO request,
      @RequestPart("image") MultipartFile image,
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
    String username = jwtUtil.extractEmail(token);
    stadiumService.createStadium(username, request, image);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<Void> updateStadium(
      @PathVariable Long id,
      @RequestPart("stadium") @Valid StadiumDTO request,
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
    String username = jwtUtil.extractEmail(token);
    stadiumService.updateStadium(username, id, request);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/all")
  @Operation(summary = "Get all stadiums")
  public ResponseEntity<List<StadiumDTO>> getAllStadiums(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
    String username = jwtUtil.extractEmail(token);
    List<Stadium> stadiums = stadiumService.getStadiums(username);
    return ResponseEntity.ok(MAPPER.toDTO(stadiums));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a stadium")
  public ResponseEntity<StadiumDTO> getStadium(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable Long id) {
    String username = jwtUtil.extractEmail(token);
    Stadium stadium = stadiumService.getStadium(username, id);
    return ResponseEntity.ok(MAPPER.toDTO(stadium));
  }

  @DeleteMapping("/delete/{id}")
  @Operation(summary = "Delete a stadium")
  public ResponseEntity<Void> deleteStadium(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable Long id) {
    String username = jwtUtil.extractEmail(token);
    stadiumService.deleteStadium(username, id);
    return ResponseEntity.ok().build();
  }
}
