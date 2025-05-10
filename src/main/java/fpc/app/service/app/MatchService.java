package fpc.app.service.app;

import fpc.app.dto.app.MatchDTO;
import fpc.app.model.app.Club;
import fpc.app.model.app.Match;
import java.util.List;

public interface MatchService {
  Match getMatch(Long id);

  Match save(Match match);

  Match create(Club homeClub, MatchDTO match);

  void update(Long matchId, MatchDTO dto);

  List<Match> getMatches(Club club);

  void deleteMatch(Long id);
}
