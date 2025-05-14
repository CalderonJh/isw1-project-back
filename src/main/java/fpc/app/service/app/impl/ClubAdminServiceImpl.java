package fpc.app.service.app.impl;

import fpc.app.exception.DataNotFoundException;
import fpc.app.model.app.Club;
import fpc.app.model.app.ClubAdmin;
import fpc.app.model.auth.User;
import fpc.app.repository.app.ClubAdminRepository;
import fpc.app.service.app.ClubAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubAdminServiceImpl implements ClubAdminService {
  private final ClubAdminRepository clubAdminRepository;

  @Override
  public ClubAdmin getClubAdmin(User user) {
    return clubAdminRepository
        .findByUser(user.getId())
        .orElseThrow(() -> new DataNotFoundException("El usuario no tiene el rol requerido"));
  }

  @Override
  public Club getClub(User admin) {
    return clubAdminRepository
        .getClubByAdmin(admin.getId())
        .orElseThrow(() -> new DataNotFoundException("No hay club asociado a este usuario"));
  }

  @Override
  public ClubAdmin save(ClubAdmin clubAdmin) {
    return clubAdminRepository.save(clubAdmin);
  }

  @Override
  public List<Club> getAllClubs() {
    return clubAdminRepository.getAllClubs();
  }
}
