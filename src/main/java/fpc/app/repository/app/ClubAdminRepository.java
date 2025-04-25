package fpc.app.repository.app;

import fpc.app.model.app.ClubAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClubAdminRepository extends JpaRepository<ClubAdmin, Long> {
	Optional<ClubAdmin> findByUserId(Long userId);
}
