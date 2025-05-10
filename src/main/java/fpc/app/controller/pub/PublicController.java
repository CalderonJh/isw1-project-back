package fpc.app.controller.pub;

import fpc.app.dto.util.Suggestion;
import fpc.app.model.app.IdentityDocument;
import fpc.app.service.auth.UserService;
import fpc.app.util.Tools;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pub")
@RequiredArgsConstructor
public class PublicController {
	private final UserService userService;
  @Operation(summary = "Get all identity document types")
  @GetMapping("/idoc-types")
  public ResponseEntity<List<Suggestion>> suggestDocTypes() {
    List<IdentityDocument> suggestions = userService.getIdentityDocumentTypes();
    return ResponseEntity.ok(suggestions.stream().map(Tools::mapSuggestion).toList());
  }
}
