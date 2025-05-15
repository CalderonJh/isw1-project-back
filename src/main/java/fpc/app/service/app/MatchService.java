package fpc.app.service.app;

import fpc.app.constant.MatchSearchType;
import fpc.app.dto.request.MatchCreationDTO;
import fpc.app.model.app.Club;
import fpc.app.model.app.Match;
import java.util.List;

public interface MatchService {
  Match getMatch(Long id);

  Match save(Match match);

  Match create(Club homeClub, MatchCreationDTO match);

  void update(Long matchId, MatchCreationDTO dto);

  List<Match> getMatches(Club club, MatchSearchType searchType, Long stadiumId);

  void deleteMatch(Long id);
}
