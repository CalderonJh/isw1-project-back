package fpc.app.repository.app;

import fpc.app.model.app.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
	List<Match> findByHomeClubId(Long homeClubId);
}
