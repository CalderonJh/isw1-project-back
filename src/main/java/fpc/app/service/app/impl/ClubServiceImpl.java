package fpc.app.service.app.impl;

import static fpc.app.util.Tools.removeExtraSpaces;

import fpc.app.dto.app.ClubCreateDTO;
import fpc.app.dto.app.ClubDTO;
import fpc.app.exception.DataNotFoundException;
import fpc.app.model.app.Club;
import fpc.app.model.app.ClubAdmin;
import fpc.app.model.auth.User;
import fpc.app.repository.app.ClubRepository;
import fpc.app.service.app.ClubAdminService;
import fpc.app.service.app.ClubService;
import fpc.app.service.auth.UserService;
import fpc.app.service.util.CloudinaryService;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {
  private final ClubRepository clubRepository;
  private final CloudinaryService cloudinaryService;
  private final UserService userService;
  private final ClubAdminService clubAdminService;

  @Override
  public Club getClub(Long clubId) {
    return clubRepository
        .findById(clubId)
        .orElseThrow(() -> new DataNotFoundException("Club no encontrado"));
  }

  @Override
  public Club getClubByAdmin(Long userId) {
    User user = userService.getUser(userId);
    return getClubByAdmin(user);
  }

  @Override
  public Club getClubByAdmin(User user) {
    ClubAdmin clubAdmin = clubAdminService.getClubAdmin(user);
    return clubAdmin.getClub();
  }

  @Override
  public void save(Club club) {
    clubRepository.save(club);
  }

  @Override
  @Transactional
  public void createClub(ClubCreateDTO request, MultipartFile file) {
    Club club = new Club();
    setClubInfo(request, file, club);
    clubRepository.save(club);
  }

  @Override
  @Transactional
  public void update(Long clubId, ClubCreateDTO request, MultipartFile file) {
    Club club = this.getClub(clubId);
    setClubInfo(request, file, club);
    clubRepository.save(club);
  }

  private void setClubInfo(ClubCreateDTO request, MultipartFile file, Club club) {
    club.setName(removeExtraSpaces(request.name()).toUpperCase());
    club.setShortName(removeExtraSpaces(request.shortName()).toUpperCase());
    if (file != null) {
      if (club.getImageId() != null) cloudinaryService.deleteImage(club.getImageId());
      String imgId = cloudinaryService.uploadImage(file);
      club.setImageId(imgId);
    }
  }

  @Override
  public List<ClubDTO> list() {
    return clubRepository.findAll().stream().map(ClubServiceImpl::map).toList();
  }

  private static ClubDTO map(Club club) {
    return ClubDTO.builder()
        .id(club.getId())
        .name(club.getName())
        .shortName(club.getShortName())
        .imageId(club.getImageId())
        .build();
  }
}
