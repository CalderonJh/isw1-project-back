package fpc.app.repository.app;

import fpc.app.model.app.Club;
import fpc.app.model.app.Subscription;
import fpc.app.model.auth.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
  @Query("SELECT s.club FROM Subscription s WHERE s.user.id = :userId")
  List<Club> getClubsByUserId(Long userId);

  @Query("SELECT s.club.id FROM Subscription s WHERE s.user.id = :userId")
  List<Long> getClubsIdsByUserId(Long userId);

  @Query("SELECT s.user FROM Subscription s WHERE s.club.id = :clubId")
  List<User> getUsersByClub(Long clubId);

  void deleteByUserIdAndClubId(Long userId, Long clubId);
}
