package fpc.app.service.app;

import fpc.app.dto.request.ClubCreateDTO;
import fpc.app.model.app.Club;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ClubService {

  void save(Club club);

  Club getClubById(Long clubId);

	void createClub(ClubCreateDTO request, MultipartFile file);

	void update(Long clubId, ClubCreateDTO request, MultipartFile file);

  List<Club> list();

	List<Club> listForMatch(Club homeTeam);

	Club getClubByAdminId(Long userId);

	List<Club> getClubsForSubscription();
}
