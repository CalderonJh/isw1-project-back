package fpc.app.controller;

import static fpc.app.util.Tools.mapSuggestion;

import fpc.app.dto.util.Suggestion;
import fpc.app.service.auth.SuperuserService;
import fpc.app.service.auth.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/su")
@Tag(name = "Superuser operations")
@RequiredArgsConstructor
public class SuperuserController {

  private final SuperuserService superuserService;
  private final UserService userService;

  @PostMapping("/give-role")
  @Operation(summary = "Assign a role to a user")
  @Parameter(name = "userId", description = "ID of the user to assign the role to")
  @Parameter(name = "roleId", description = "ID of the role to assign to the user")
  public ResponseEntity<Void> giveRole(@RequestParam Long userId, @RequestParam Long roleId) {
    superuserService.giveRole(userId, roleId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/revoke-role")
  @Operation(summary = "Revoke a role from a user")
  @Parameter(name = "userId", description = "ID of the user to revoke the role from")
  @Parameter(name = "roleId", description = "ID of the role to revoke from the user")
  public ResponseEntity<Void> revokeRole(@RequestParam Long userId, @RequestParam Long roleId) {
    superuserService.revokeRole(userId, roleId);
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
