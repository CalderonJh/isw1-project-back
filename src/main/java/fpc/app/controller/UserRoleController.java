package fpc.app.controller;

import static fpc.app.util.Tools.mapSuggestion;

import fpc.app.dto.response.UserRoleDTO;
import fpc.app.dto.util.Suggestion;
import fpc.app.security.JwtUtil;
import fpc.app.service.auth.UserRoleService;
import fpc.app.service.auth.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/su")
@Tag(name = "Superuser operations")
@RequiredArgsConstructor
public class UserRoleController {
  private final UserRoleService userRoleService;
  private final UserService userService;
  private final JwtUtil jwtUtil;

  @GetMapping("/user-role")
  @Operation(summary = "Get user roles info, if not provided, uses the current user ID")
  @Parameter(name = "userId", description = "ID of the user to get roles for")
  public ResponseEntity<UserRoleDTO> getUserRoles(
      @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
      @Positive Long userId) {
    if (userId == null) userId = jwtUtil.getUserId(token);
    UserRoleDTO userRoles = userRoleService.getUserRoles(userId);
    return ResponseEntity.ok(userRoles);
  }

  @PostMapping("/give-role")
  @Operation(summary = "Assign a role to a user")
  @Parameter(name = "userId", description = "ID of the user to assign the role to")
  @Parameter(name = "roleId", description = "ID of the role to assign to the user")
  @Parameter(name = "clubId", description = "Required if the role is club admin")
  public ResponseEntity<Void> giveRole(
      @RequestParam Long userId,
      @RequestParam Long roleId,
      @RequestParam(required = false) Long clubId) {
    userRoleService.giveRole(userId, roleId, clubId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/revoke-role")
  @Operation(summary = "Revoke a role from a user")
  @Parameter(name = "userId", description = "ID of the user to revoke the role from")
  @Parameter(name = "roleId", description = "ID of the role to revoke from the user")
  public ResponseEntity<Void> revokeRole(@RequestParam Long userId, @RequestParam Long roleId) {
    userRoleService.revokeRole(userId, roleId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/list-users")
  @Operation(summary = "List all users")
  @Parameter(name = "kw", description = "Keyword to search for users by email, name or last name")
  public ResponseEntity<List<Suggestion>> listUsers(
      @RequestParam(name = "kw", required = false) String keyword) {
    List<Suggestion> response =
        userService.list(keyword).stream()
            .map(user -> mapSuggestion(user.getId(), user.getUsername()))
            .toList();
    return ResponseEntity.ok(response);
  }

  @GetMapping("/list-roles")
  @Operation(summary = "List all roles")
  public ResponseEntity<List<Suggestion>> listRoles() {
    List<Suggestion> response =
        userService.listRoles().stream()
            .map(role -> mapSuggestion(role.getId(), role.getName()))
            .toList();
    return ResponseEntity.ok(response);
  }
}
