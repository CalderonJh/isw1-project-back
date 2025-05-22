package fpc.app.repository.app;

import fpc.app.model.app.Stadium;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StadiumRepository extends JpaRepository<Stadium, Long> {
  boolean existsByNameIgnoreCaseAndClubId(String name, Long clubId);

  List<Stadium> findByClubId(Long id);

  @Query("select s.club.id from Stadium s where s.id = :stadiumId")
  Long getClubId(Long stadiumId);
}
