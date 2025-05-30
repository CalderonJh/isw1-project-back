package fpc.app.controller;


import fpc.app.dto.request.LoginRequest;
import fpc.app.dto.UserDTO;
import fpc.app.service.auth.impl.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Registration and authentication")
@SecurityRequirement(name = "")
public class AuthController {
  private final AuthenticationService authService;

  @PostMapping("/login")
  @Operation(summary = "Use credentials to generate token")
  public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest loginRequest) {
    String token = authService.login(loginRequest.username(), loginRequest.password());
    return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token).build();
  }

  @PostMapping("/register")
  @ApiResponse(responseCode = "200", description = "User registered successfully")
  @ApiResponse(responseCode = "409", description = "If validation fails")
  @Operation(summary = "Register a new user and generate token")
  public ResponseEntity<Void> register(@RequestBody @Valid UserDTO request) {
    String token = authService.register(request);
    return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token).build();
  }
}
