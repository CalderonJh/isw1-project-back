package fpc.app.repository.app;

import fpc.app.model.app.SeasonPassType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SeasonPassTypeRepository extends JpaRepository<SeasonPassType, Long> {
	@Query("select s from SeasonPassType s where s.seasonPassOfferId = :seasonPassOfferId")
  List<SeasonPassType> getSeasonPassTypes(Long seasonPassOfferId);
}
