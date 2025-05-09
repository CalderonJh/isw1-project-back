package fpc.app.repository.app;

import fpc.app.model.app.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
	List<Match> findByHomeClubId(Long homeClubId);

	@Query("select m.homeClub.id from Match m where m.id = :recordId")
	Long getHomeClubId(Long recordId);
}
