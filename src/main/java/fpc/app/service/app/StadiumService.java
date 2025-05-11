package fpc.app.service.app;

import fpc.app.dto.app.StadiumDTO;
import fpc.app.model.app.Club;
import fpc.app.model.app.Stadium;
import java.util.List;

import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

public interface StadiumService {
  void createStadium(Club club, StadiumDTO dto, MultipartFile image);

  void updateStadium(Long stadiumId, StadiumDTO dto);

  void deleteStadium(Long id);

  Stadium getStadium(Long id);

  List<Stadium> getStadiums(Club club);

  void updateStadiumImage(Long id, @NonNull MultipartFile image);
}
