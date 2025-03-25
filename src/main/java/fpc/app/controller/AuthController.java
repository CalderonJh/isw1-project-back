package fpc.app.controller;

import fpc.app.dto.app.RegisterUserRequest;
import fpc.app.dto.auth.JwtResponse;
import fpc.app.dto.auth.LoginRequest;
import fpc.app.service.auth.UserService;
import fpc.app.service.auth.impl.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthenticationService authService;
  private final UserService userService;
  @PostMapping("/login")
  public ResponseEntity<JwtResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
    String token = authService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
    return ResponseEntity.ok(new JwtResponse(token));
  }

  @PostMapping("/register")
  public ResponseEntity<JwtResponse> register(@RequestBody @Valid RegisterUserRequest request) {
    String token = userService.registerUser(request);
    return ResponseEntity.ok(new JwtResponse(token));
  }

  @GetMapping("/test")
  public ResponseEntity<String> test() {
    return ResponseEntity.ok("You are authenticated");
  }
}
