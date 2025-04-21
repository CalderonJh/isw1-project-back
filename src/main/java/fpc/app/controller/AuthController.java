package fpc.app.controller;


import fpc.app.dto.app.RegisterUserRequest;
import fpc.app.dto.auth.LoginRequest;
import fpc.app.dto.util.Suggestion;
import fpc.app.model.app.IdentityDocument;
import fpc.app.service.auth.impl.AuthenticationService;
import fpc.app.util.Tools;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Registration and authentication")
public class AuthController {
  private final AuthenticationService authService;

  @Operation(summary = "Use credentials to generate token")
  @PostMapping("/login")
  public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest loginRequest) {
    String token = authService.login(loginRequest.getUsername(), loginRequest.getPassword());
    return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token).build();
  }

  @Operation(summary = "Register a new user and generate token")
  @ApiResponse(responseCode = "200", description = "User registered successfully")
  @ApiResponse(responseCode = "409", description = "If validation fails")
  @PostMapping("/register")
  public ResponseEntity<Void> register(@RequestBody @Valid RegisterUserRequest request) {
    String token = authService.register(request);
    return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token).build();
  }

  @Operation(summary = "Get all identity document types")
  @GetMapping("/doc-types")
  public ResponseEntity<List<Suggestion>> suggestDocTypes() {
    List<IdentityDocument> suggestions = authService.getIdentityDocumentTypes();
    return ResponseEntity.ok(suggestions.stream().map(Tools::mapSuggestion).toList());
  }
}
