package fpc.app.repository.app;

import fpc.app.model.app.Club;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClubRepository extends JpaRepository<Club, Long> {

  @Query("select c from Club c where c.admin is not null and c.admin.id = :adminId")
  Optional<Club> getClubByAdmin(Long adminId);

  @Query("select distinct c from Club c where c.admin is not null")
  List<Club> getClubsWithAdmin();

  @Query("select count(c) > 0 from Club c where c.id = :clubId and c.admin is not null and c.admin.id = :userId")
  boolean existsPermission(Long userId, Long clubId);

  @Query("select count(c) > 0 from Club c where c.admin is not null and c.admin.id = :userId")
  boolean userIsAdmin(Long userId);
}
