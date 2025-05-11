package fpc.app.service.app.impl;

import static fpc.app.util.Tools.*;

import fpc.app.dto.app.StadiumDTO;
import fpc.app.dto.app.StandDTO;
import fpc.app.exception.DataNotFoundException;
import fpc.app.model.app.Club;
import fpc.app.model.app.Stadium;
import fpc.app.model.app.Stand;
import fpc.app.repository.app.StadiumRepository;
import fpc.app.service.app.StadiumService;
import fpc.app.service.util.CloudinaryService;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class StadiumServiceImpl implements StadiumService {
  private final StadiumRepository stadiumRepository;
  private final CloudinaryService cloudinaryService;

  @Override
  @Transactional
  public void createStadium(Club club, StadiumDTO dto, MultipartFile image) {
    validateStadiumName(dto.name(), club);
    Stadium stadium = Stadium.builder().name(dto.name()).club(club).build();
    addStands(stadium, dto.stands());
    String imageId = saveImage(image);
    stadium.setImageId(imageId);
    stadiumRepository.save(stadium);
  }

  private void validateStadiumName(String name, Club club) {
    if (stadiumRepository.existsByNameIgnoreCaseAndClubId(removeExtraSpaces(name), club.getId()))
      throw new DataNotFoundException("Ya existe un estadio con ese nombre para este club");
  }

  private String saveImage(MultipartFile image) {
    return cloudinaryService.uploadImage(image);
  }

  private void addStands(Stadium stadium, List<StandDTO> stands) {
    stands.forEach(stand -> stadium.addStand(new Stand(stand.name(), stand.capacity())));
  }

  @Override
  @Transactional
  public void updateStadium(Long stadiumId, StadiumDTO dto) {
    Stadium stadium = getStadium(stadiumId);
    String name = removeExtraSpaces(dto.name());
    if (!equalsText(stadium.getName(), name)) validateStadiumName(dto.name(), stadium.getClub());
    stadium.setName(name);
    stadium.clearStands();
    addStands(stadium, dto.stands());
    stadiumRepository.save(stadium);
  }

  @Override
  @Transactional
  public void updateStadiumImage(Long id, @NonNull MultipartFile image) {
    Stadium stadium = getStadium(id);
    if (stadium.getImageId() != null) cloudinaryService.deleteImage(stadium.getImageId());
    String imageId = cloudinaryService.uploadImage(image);
    stadium.setImageId(imageId);
    stadiumRepository.save(stadium);
  }

  @Override
  public void deleteStadium(Long id) {
    Stadium stadium = getStadium(id);
    stadiumRepository.delete(stadium);
  }

  @Override
  public Stadium getStadium(Long id) {
    return stadiumRepository
        .findById(id)
        .orElseThrow(() -> new DataNotFoundException("Estadio no encontrado"));
  }

  @Override
  public List<Stadium> getStadiums(Club club) {
    return stadiumRepository.findByClubId(club.getId());
  }
}
