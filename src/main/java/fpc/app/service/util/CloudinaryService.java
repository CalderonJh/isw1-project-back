package fpc.app.service.util;

import static java.util.Objects.requireNonNull;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import fpc.app.exception.TechnicalException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

  private final Cloudinary cloudinary;

  public String uploadImage(MultipartFile file) {
    try {
      Map<String, String> options = new HashMap<>();
      options.put("resource_type", "auto");
      return cloudinary.uploader().upload(file.getBytes(), options).get("public_id").toString();
    } catch (IOException e) {
      throw new TechnicalException("Image upload failed", e);
    }
  }

  public String deleteImage(String publicId) {
    try {
      Map result = cloudinary.uploader().destroy(publicId, Map.of());
      return (String) result.get("result");
    } catch (IOException e) {
      throw new TechnicalException("Image deletion failed", e);
    }
  }

  public String getImageUrl(String publicId) {
    return cloudinary.url().generate(publicId);
  }

  public String getResizedImageUrl(String publicId, int width, int height) {
    return cloudinary
        .url()
        .transformation(new Transformation().width(width).height(height).crop("fill"))
        .generate(publicId);
  }
}
