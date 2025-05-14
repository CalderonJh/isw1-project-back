package fpc.app.service.app;

import fpc.app.model.app.Club;
import fpc.app.model.app.ClubAdmin;
import fpc.app.model.auth.User;

import java.util.List;

public interface ClubAdminService {
  List<Club> getAllClubs();

  ClubAdmin getClubAdmin(User user);

  ClubAdmin save(ClubAdmin clubAdmin);

  Club getClub(User admin);
}
