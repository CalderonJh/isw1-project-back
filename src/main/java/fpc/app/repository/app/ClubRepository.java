package fpc.app.repository.app;

import fpc.app.model.app.Club;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubRepository extends JpaRepository<Club, Long> {}
