package fpc.app.repository.app;

import fpc.app.model.app.SeasonPassType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SeasonPassTypeRepository extends JpaRepository<SeasonPassType, Long> {
	@Query("select s from SeasonPassType s where s.seasonPassOfferId = :seasonPassOfferId")
  List<SeasonPassType> getSeasonPassTypes(Long seasonPassOfferId);

  @Query(
      nativeQuery = true,
      value =
          "select v.available_ticket from app.season_pass_purchases v where v.season_pass_type_id = :seasonPassTypeId")
  Boolean isAvailable(Long seasonPassTypeId);

  SeasonPassType findBySeasonPassOfferIdAndStandId(Long id, Long standId);
}
