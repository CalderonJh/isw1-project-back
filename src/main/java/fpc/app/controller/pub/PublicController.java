package fpc.app.controller.pub;

import fpc.app.dto.app.ClubDTO;
import fpc.app.dto.util.Suggestion;
import fpc.app.mapper.ClubMapper;
import fpc.app.model.app.Club;
import fpc.app.model.app.IdentityDocument;
import fpc.app.service.app.ClubService;
import fpc.app.service.auth.UserService;
import fpc.app.util.Tools;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pub")
@RequiredArgsConstructor
@Tag(name = "Public endpoints")
public class PublicController {
	private final UserService userService;
  private final ClubService clubService;

  @GetMapping("/idoc-types")
  @Operation(summary = "Get all identity document types")
  public ResponseEntity<List<Suggestion>> suggestDocTypes() {
    List<IdentityDocument> suggestions = userService.getIdentityDocumentTypes();
    return ResponseEntity.ok(suggestions.stream().map(Tools::mapSuggestion).toList());
  }

  @GetMapping("/club/list")
  @Operation(summary = "List all clubs")
  public ResponseEntity<List<ClubDTO>> list() {
    List<Club> clubs = clubService.list();
    return ResponseEntity.ok(ClubMapper.map(clubs));
  }
}
