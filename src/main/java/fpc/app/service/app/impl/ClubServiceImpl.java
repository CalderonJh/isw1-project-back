package fpc.app.service.app.impl;

import static fpc.app.util.Tools.removeExtraSpaces;
import static java.util.Objects.requireNonNull;

import fpc.app.dto.app.ClubRequest;
import fpc.app.model.app.Club;
import fpc.app.repository.app.ClubRepository;
import fpc.app.service.app.ClubService;
import fpc.app.service.util.CloudinaryService;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ClubServiceImpl implements ClubService {
  private final ClubRepository clubRepository;
  private final CloudinaryService cloudinaryService;

  public ClubServiceImpl(ClubRepository clubRepository, CloudinaryService cloudinaryService) {
    this.clubRepository = clubRepository;
    this.cloudinaryService = cloudinaryService;
  }

  @Override
  @Nullable
  public Club getClub(Long clubId) {
    return clubRepository.findById(clubId).orElse(null);
  }

  @Override
  public void save(Club club) {
    clubRepository.save(club);
  }

  @Override
  @Transactional
  public void createClub(ClubRequest request, MultipartFile file) {
    Club club = new Club();
    setClubInfo(request, file, club);
    clubRepository.save(club);
  }

  @Override
  @Transactional
  public void update(Long clubId, ClubRequest request, MultipartFile file) {
    Club club = requireNonNull(this.getClub(clubId));
    setClubInfo(request, file, club);
    clubRepository.save(club);
  }

  private void setClubInfo(ClubRequest request, MultipartFile file, Club club) {
    club.setName(removeExtraSpaces(request.getName()).toUpperCase());
    club.setShortName(removeExtraSpaces(request.getShortName()).toUpperCase());
    if (file != null) {
      if (club.getImageId() != null) cloudinaryService.deleteImage(club.getImageId());
      String imgId = cloudinaryService.uploadImage(file).get("public_id").toString();
      club.setImageId(imgId);
    }
  }
}
