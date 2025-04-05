package fpc.app.controller;

import static fpc.app.constant.Constant.TOKEN_COOKIE_NAME;
import static fpc.app.constant.Constant.TOKEN_EXPIRATION_TIME;

import fpc.app.dto.app.RegisterUserRequest;
import fpc.app.dto.auth.LoginRequest;
import fpc.app.service.auth.impl.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Registration and authentication")
public class AuthController {
  private final AuthenticationService authService;

  @PostMapping("/login")
  public ResponseEntity<Void> login(
      @RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) {
    String token = authService.login(loginRequest.getUsername(), loginRequest.getPassword());

    Cookie cookie = new Cookie(TOKEN_COOKIE_NAME, token);
    cookie.setHttpOnly(true);
    // cookie.setSecure(true); // Solo en HTTPS (desactivar en desarrollo)
    cookie.setPath("/");
    cookie.setMaxAge(TOKEN_EXPIRATION_TIME);

    response.addCookie(cookie);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/logout")
  public ResponseEntity<Void> logout(HttpServletResponse response) {
    Cookie cookie = new Cookie(TOKEN_COOKIE_NAME, null);
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setPath("/");
    cookie.setMaxAge(0);

    response.addCookie(cookie);
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "Register a new user and generate token")
  @ApiResponse(responseCode = "200", description = "User registered successfully")
  @ApiResponse(responseCode = "409", description = "If validation fails")
  @PostMapping("/register")
  public ResponseEntity<Void> register(
      @RequestBody @Valid RegisterUserRequest request, HttpServletResponse response) {
    String token = authService.register(request);

    Cookie cookie = new Cookie(TOKEN_COOKIE_NAME, token);
    cookie.setHttpOnly(true);
    // cookie.setSecure(true); // Solo en HTTPS (desactivar en desarrollo)
    cookie.setPath("/");
    cookie.setMaxAge(TOKEN_EXPIRATION_TIME);

    response.addCookie(cookie);
    return ResponseEntity.ok().build();
  }
}
