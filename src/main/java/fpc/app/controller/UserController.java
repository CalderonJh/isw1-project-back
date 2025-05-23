package fpc.app.controller;

import fpc.app.dto.request.UpdateUserDTO;
import fpc.app.dto.request.UpdateUserPasswordDTO;
import fpc.app.dto.UserDTO;
import fpc.app.mapper.UserMapper;
import fpc.app.model.app.Club;
import fpc.app.model.auth.User;
import fpc.app.security.JwtUtil;
import fpc.app.service.app.ClubService;
import fpc.app.service.app.SubscriptionService;
import fpc.app.service.auth.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User operations")
public class UserController {

  private final UserService userService;
  private final JwtUtil jwtUtil;
  private final ClubService clubService;
  private final SubscriptionService subscriptionService;

  @PutMapping("/update")
  public ResponseEntity<Void> updateUserInf(
      @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
      @RequestBody @Valid UpdateUserDTO update) {
    Long userId = jwtUtil.getUserId(token);
    User user = userService.getUser(userId);
    userService.updateUserInfo(user, update);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/info")
  public ResponseEntity<UserDTO> getUserInfo(
      @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
    Long userId = jwtUtil.getUserId(token);
    User user = userService.getUser(userId);
    return ResponseEntity.ok(UserMapper.map(user));
  }

  @PutMapping("/update/password")
  public ResponseEntity<Void> updatePassword(
      @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
      @RequestBody @Valid UpdateUserPasswordDTO update) {
    Long userId = jwtUtil.getUserId(token);
    User user = userService.getUser(userId);
    userService.updatePassword(user, update.password());
    return ResponseEntity.ok().build();
  }

  @PostMapping("/subscribe")
  @Parameter(name = "clubId", description = "Club ID")
  public ResponseEntity<Void> subscribeToClub(
      @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
      @RequestParam Long clubId) {
    User user = userService.getUser(jwtUtil.getUserId(token));
    Club club = clubService.getClubById(clubId);
    subscriptionService.subscribe(user, club);
    return ResponseEntity.ok().build();
  }
}
