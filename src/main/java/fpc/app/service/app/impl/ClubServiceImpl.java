package fpc.app.service.app.impl;

import static fpc.app.util.Tools.removeExtraSpaces;

import fpc.app.dto.request.ClubCreateDTO;
import fpc.app.exception.DataNotFoundException;
import fpc.app.model.app.Club;
import fpc.app.repository.app.ClubRepository;
import fpc.app.service.app.ClubService;
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

  @Override
  public Club getClubById(Long clubId) {
    return clubRepository
        .findById(clubId)
        .orElseThrow(() -> new DataNotFoundException("Club no encontrado"));
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
    Club club = this.getClubById(clubId);
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
  public List<Club> list() {
    return clubRepository.findAll();
  }

  @Override
  public List<Club> listForMatch(Club homeTeam) {
    var clubs = clubRepository.findAll();
    return clubs.stream().filter(club -> !club.getId().equals(homeTeam.getId())).toList();
  }

  @Override
  public Club getClubByAdminId(Long userId) {
    return clubRepository
        .getClubByAdmin(userId)
        .orElseThrow(() -> new DataNotFoundException("No hay club asociado a este usuario"));
  }

  @Override
  public List<Club> getClubsForSubscription() {
    return clubRepository.getClubsWithAdmin();
  }
}
