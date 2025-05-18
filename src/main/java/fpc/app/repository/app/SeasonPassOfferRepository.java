package fpc.app.repository.app;

import fpc.app.model.app.SeasonPassOffer;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SeasonPassOfferRepository extends JpaRepository<SeasonPassOffer, Long> {
  @Query(
      "select o from SeasonPassOffer o where o.isPaused = false and o.startDate <= :colTime and o.endDate >= :colTime")
  List<SeasonPassOffer> findAllActiveByClubIdIn(List<Long> clubIds, LocalDateTime colTime);

  @Query("select o from SeasonPassOffer o where o.isPaused = false and o.startDate <= :colTime and o.endDate >= :colTime")
  List<SeasonPassOffer> findAllActive(LocalDateTime colTime);
}
