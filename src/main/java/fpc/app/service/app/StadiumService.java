package fpc.app.service.app;

import fpc.app.dto.app.StadiumDTO;
import fpc.app.model.app.Stadium;
import jakarta.annotation.Nullable;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface StadiumService {
  void createStadium(String username, StadiumDTO dto, MultipartFile image);

  void updateStadium(String username, Long stadiumId, StadiumDTO dto);

  void deleteStadium(String username, Long id);

  @Nullable
  Stadium getStadium(String username, Long id);

  @Nullable
  Stadium getStadium(Long id);

  List<Stadium> getStadiums(String username);
}
