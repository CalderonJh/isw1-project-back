package fpc.app.repository.app;

import fpc.app.model.app.Club;
import fpc.app.model.app.ClubAdmin;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClubAdminRepository extends JpaRepository<ClubAdmin, Long> {
  @Query("select ca from ClubAdmin ca where ca.user.id = :userId")
  Optional<ClubAdmin> findByUser(Long userId);

  @Query("select ca.club from ClubAdmin ca where ca.user.id = :adminId")
  Optional<Club> getClubByAdmin(Long adminId);

  @Query("select distinct ca.club from ClubAdmin ca")
  List<Club> getAllClubs();
}
