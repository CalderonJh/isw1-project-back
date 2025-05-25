package fpc.app.controller;

import fpc.app.dto.response.ClubResponseDTO;
import fpc.app.mapper.ClubMapper;
import fpc.app.model.app.Club;
import fpc.app.model.auth.User;
import fpc.app.security.JwtUtil;
import fpc.app.service.app.ClubService;
import fpc.app.service.auth.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/club-admin")
@RestController
@RequiredArgsConstructor
@Tag(name = "Club admin operations", description = "Endpoints for club administrators")
public class ClubAdminController {
  private final UserService userService;
  private final ClubService clubService;
	private final JwtUtil jwtUtil;

	@GetMapping("/my-club")
  @Operation(summary = "Get club information for the admin")
  public ResponseEntity<ClubResponseDTO> getClub(
      @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
    User user = userService.getUser(jwtUtil.getUserId(token));
    Club club = clubService.getClubByAdminId(user.getId());
    return ResponseEntity.ok(ClubMapper.map(club, false));
  }
}
