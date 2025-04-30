package fpc.app.repository.app;

import fpc.app.model.app.Stadium;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StadiumRepository extends JpaRepository<Stadium, Long> {
  boolean existsByNameIgnoreCaseAndClubId(String name, Long clubId);

  List<Stadium> findByClubId(Long id);
}
