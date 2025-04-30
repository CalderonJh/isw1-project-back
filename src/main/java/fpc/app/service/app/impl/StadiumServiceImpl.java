package fpc.app.service.app.impl;

import static fpc.app.util.Tools.equalsText;
import static fpc.app.util.Tools.removeExtraSpaces;
import static java.util.Objects.requireNonNull;

import fpc.app.dto.app.StadiumDTO;
import fpc.app.dto.app.StandDTO;
import fpc.app.exception.DataNotFoundException;
import fpc.app.exception.ValidationException;
import fpc.app.model.app.Club;
import fpc.app.model.app.ClubAdmin;
import fpc.app.model.app.Stadium;
import fpc.app.model.app.Stand;
import fpc.app.model.auth.User;
import fpc.app.repository.app.ClubAdminRepository;
import fpc.app.repository.app.StadiumRepository;
import fpc.app.service.app.ClubService;
import fpc.app.service.app.StadiumService;
import fpc.app.service.auth.UserService;
import fpc.app.service.util.CloudinaryService;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class StadiumServiceImpl implements StadiumService {
  private final UserService userService;
  private final ClubAdminRepository clubAdminRepository;
  private final StadiumRepository stadiumRepository;
  private final CloudinaryService cloudinaryService;
  private final ClubService clubService;

  @Override
  @Transactional
  public void createStadium(String username, StadiumDTO dto, MultipartFile image) {
    User user = requireNonNull(userService.getByUsername(username));
    ClubAdmin clubAdmin =
        clubAdminRepository
            .findByUserId(user.getId())
            .orElseThrow(() -> new DataNotFoundException("User is not a club admin"));
    validateStadiumName(dto.name(), clubAdmin.getClub());
    Stadium stadium = Stadium.builder().name(dto.name()).club(clubAdmin.getClub()).build();
    addStands(stadium, dto.stands());
    saveImage(stadium, image);
    stadiumRepository.save(stadium);
  }

  private void validateStadiumName(String name, Club club) {
    if (stadiumRepository.existsByNameIgnoreCaseAndClubId(removeExtraSpaces(name), club.getId()))
      throw new DataNotFoundException("Ya existe un estadio con ese nombre para este club");
  }

  private void saveImage(Stadium stadium, MultipartFile image) {
    String imageId = cloudinaryService.uploadImage(image);
    stadium.setImageId(imageId);
  }

  private void addStands(Stadium stadium, List<StandDTO> stands) {
    stands.forEach(stand -> stadium.addStand(new Stand(stand.name(), stand.capacity())));
  }

  @Override
  public void updateStadium(String username, Long stadiumId, StadiumDTO dto) {
    Stadium stadium = requireNonNull(getStadium(username, stadiumId));
    validateStadiumAccess(username, stadium.getClub());
    String name = removeExtraSpaces(dto.name());
    if (!equalsText(stadium.getName(), name)) validateStadiumName(dto.name(), stadium.getClub());
    stadium.setName(name);
    stadium.clearStands();
    addStands(stadium, dto.stands());
    stadiumRepository.save(stadium);
  }

  private void validateStadiumAccess(String username, @NotNull Club club) {
    User user = userService.getByUsername(username);
    if (!clubAdminRepository.existsByUserIdAndClubId(user.getId(), club.getId()))
      throw new ValidationException("No access to this stadium");
  }

  @Override
  public void deleteStadium(String username, Long id) {
    Stadium stadium = requireNonNull(getStadium(username, id));
    validateStadiumAccess(username, stadium.getClub());
    stadiumRepository.delete(stadium);
  }

  @Nullable
  @Override
  public Stadium getStadium(String username, Long id) {
    Stadium stadium = stadiumRepository.findById(id).orElseThrow();
    validateStadiumAccess(username, stadium.getClub());
    return stadium;
  }

  @Override
  public List<Stadium> getStadiums(String username) {
    Club club = clubService.getClubByAdmin(username);
    return stadiumRepository.findByClubId(club.getId());
  }
}
