package fpc.app.controller;

import fpc.app.dto.user.UpdateUserDTO;
import fpc.app.dto.user.UpdateUserPasswordDTO;
import fpc.app.dto.user.UserDTO;
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
    return ResponseEntity.ok(map(user));
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
    Club club = clubService.getClub(clubId);
    subscriptionService.subscribe(user, club);
    return ResponseEntity.ok().build();
  }

  private UserDTO map(User user) {
    return UserDTO.builder()
        .name(user.getPerson().getName())
        .lastName(user.getPerson().getLastName())
        .email(user.getUsername())
        .documentTypeId(user.getPerson().getDocumentType().getId())
        .documentNumber(user.getPerson().getDocumentNumber())
        .gender(user.getPerson().getGender())
        .birthDate(user.getPerson().getBirthday())
        .phoneNumber(user.getPerson().getPhone())
        .build();
  }
}
