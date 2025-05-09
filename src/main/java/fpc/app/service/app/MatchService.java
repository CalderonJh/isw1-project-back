package fpc.app.service.app;

import fpc.app.dto.app.MatchDTO;
import fpc.app.model.app.Match;
import java.util.List;

public interface MatchService {
  Match getMatch(Long id);

  Match save(Match match);

  Match create(String clubAdminUsername, MatchDTO match);

  void update(String username, MatchDTO dto);

  List<Match> getMatches(String username);

  void deleteMatch(String username, Long id);
}
