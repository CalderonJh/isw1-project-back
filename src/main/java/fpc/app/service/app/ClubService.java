package fpc.app.service.app;

import fpc.app.dto.app.ClubRequest;
import fpc.app.model.app.Club;
import jakarta.annotation.Nullable;
import org.springframework.web.multipart.MultipartFile;

public interface ClubService {

  void save(Club club);

  @Nullable
  Club getClub(Long clubId);

	void createClub(ClubRequest request, MultipartFile file);

	void update(Long clubId, ClubRequest request, MultipartFile file);
}
