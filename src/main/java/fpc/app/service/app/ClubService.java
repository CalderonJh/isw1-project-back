package fpc.app.service.app;

import fpc.app.dto.app.ClubCreateDTO;
import fpc.app.dto.app.ClubDTO;
import fpc.app.model.app.Club;
import fpc.app.model.auth.User;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ClubService {

  Club getClubByAdmin(Long userId);


	Club getClubByAdmin(User user);

  void save(Club club);

  Club getClub(Long clubId);

	void createClub(ClubCreateDTO request, MultipartFile file);

	void update(Long clubId, ClubCreateDTO request, MultipartFile file);

  List<ClubDTO> list();
}
