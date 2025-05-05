package fpc.app.service.app;

import fpc.app.dto.app.ClubDTO;
import fpc.app.dto.app.ClubCreateDTO;
import fpc.app.model.app.Club;
import jakarta.annotation.Nullable;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ClubService {

  Club getClubByAdmin(String username);

  void save(Club club);

  @Nullable
  Club getClub(Long clubId);

	void createClub(ClubCreateDTO request, MultipartFile file);

	void update(Long clubId, ClubCreateDTO request, MultipartFile file);

  List<ClubDTO> list();
}
