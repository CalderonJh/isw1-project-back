package fpc.app.controller;

import fpc.app.service.util.CloudinaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
@Tag(name = "Image Management", description = "Endpoints for managing images")
public class CloudinaryController {
  private final CloudinaryService cloudinaryService;

  @GetMapping("/{publicId}")
  @Operation(description = "Get image URL by public ID")
  public ResponseEntity<Map<String, String>> getImage(@PathVariable String publicId) {
    String url = cloudinaryService.getImageUrl(publicId);
    Map<String, String> response = new HashMap<>();
    response.put("url", url);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/upload")
  @Operation(description = "Upload an image")
  public ResponseEntity<Void> uploadImage(@RequestPart("file") MultipartFile file) {
    System.out.println("Uploading image: " + file.getOriginalFilename());
    cloudinaryService.uploadImage(file);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{publicId}")
  @Operation(description = "Delete an image by public ID")
  public ResponseEntity<String> deleteImage(@PathVariable String publicId) {
    String result = cloudinaryService.deleteImage(publicId);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/{publicId}/resize")
  @Operation(description = "Get resized image URL by public ID")
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
