package fpc.app.repository.app;

import fpc.app.model.app.ClubAdmin;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClubAdminRepository extends JpaRepository<ClubAdmin, Long> {
	Optional<ClubAdmin> findByUserId(Long userId);
	boolean existsByUserIdAndClubId(Long userId, Long clubId);

  @Query("select ca from ClubAdmin ca where ca.user.username = :username")
  Optional<ClubAdmin> findByUser(String username);
}
