package fpc.app.controller;

import fpc.app.dto.user.UpdateUserDTO;
import fpc.app.dto.user.UpdateUserPasswordDTO;
import fpc.app.dto.user.UserDTO;
import fpc.app.model.auth.User;
import fpc.app.security.JwtUtil;
import fpc.app.service.auth.UserService;
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

  @PutMapping("/update")
  public ResponseEntity<Void> updateUserInf(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
      @RequestBody @Valid UpdateUserDTO update) {
    Long userId = jwtUtil.getUserId(token);
    User user = userService.getUser(userId);
    userService.updateUserInfo(user, update);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/info")
  public ResponseEntity<UserDTO> getUserInfo(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
    Long userId = jwtUtil.getUserId(token);
    User user = userService.getUser(userId);
    return ResponseEntity.ok(map(user));
  }

  @PutMapping("/update/password")
  public ResponseEntity<Void> updatePassword(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
      @RequestBody @Valid UpdateUserPasswordDTO update) {
    Long userId = jwtUtil.getUserId(token);
    User user = userService.getUser(userId);
    userService.updatePassword(user, update.password());
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
