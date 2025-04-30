package fpc.app.controller;

import fpc.app.service.util.CloudinaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
@Tag(name = "Image Management", description = "Endpoints for managing images")
public class CloudinaryController {
  private final CloudinaryService cloudinaryService;

  @GetMapping("/{publicId}")
  @Operation(summary = "Get image URL by public ID")
  public ResponseEntity<Map<String, String>> getImage(@PathVariable String publicId) {
    String url = cloudinaryService.getImageUrl(publicId);
    Map<String, String> response = new HashMap<>();
    response.put("url", url);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{publicId}/resize")
  @Operation(summary = "Get resized image URL by public ID")
  public ResponseEntity<Map<String, String>> getResizedImage(
      @PathVariable String publicId,
      @RequestParam(defaultValue = "300") int width,
      @RequestParam(defaultValue = "300") int height) {
    String url = cloudinaryService.getResizedImageUrl(publicId, width, height);
    Map<String, String> response = new HashMap<>();
    response.put("url", url);
    return ResponseEntity.ok(response);
  }
}
