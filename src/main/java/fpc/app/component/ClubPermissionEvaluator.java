package fpc.app.component;

import fpc.app.model.app.Club;
import fpc.app.model.app.ClubAdmin;
import fpc.app.model.app.Match;
import fpc.app.model.app.TicketOffer;
import fpc.app.repository.TicketOfferRepository;
import fpc.app.repository.app.ClubAdminRepository;
import fpc.app.repository.app.MatchRepository;
import java.io.Serializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class ClubPermissionEvaluator implements PermissionEvaluator {
  private ClubAdminRepository clubAdminRepository;
  private TicketOfferRepository ticketOfferRepository;
  private MatchRepository matchRepository;

  @Override
  public boolean hasPermission(
      Authentication authentication, Object targetDomainObject, Object permission) {
    if (authentication == null || targetDomainObject == null) {
      return false;
    }

    String username = authentication.getName();
    ClubAdmin admin = clubAdminRepository.findByUser(username).orElse(null);

    if (admin == null) {
      return false;
    }

    Long adminClubId = admin.getClub().getId();

    // Verificar según el tipo de objeto
    return switch (targetDomainObject) {
      case Club club -> adminClubId.equals((club).getId());
      case TicketOffer offer -> adminClubId.equals((offer).getPostedBy().getClub().getId());
      case Match match -> adminClubId.equals((match.getHomeClub().getId()));
      default -> false;
    };
  }

  @Override
  public boolean hasPermission(
      Authentication authentication, Serializable targetId, String targetType, Object permission) {
    // Implementación para casos donde solo tenemos IDs
    if (authentication == null || targetId == null || targetType == null) {
      return false;
    }

    String username = authentication.getName();
    ClubAdmin admin = clubAdminRepository.findByUser(username).orElse(null);

    if (admin == null) {
      return false;
    }

    Long clubId = admin.getClub().getId();

    Long recordId = Long.valueOf(targetId.toString());
    return switch (targetType) {
      case "Match" -> clubId.equals(matchRepository.getHomeClubId(recordId));
      case "Club" -> clubId.equals(recordId);
      case "TicketOffer" -> clubId.equals(ticketOfferRepository.getClubId(recordId));
      default -> false;
    };
  }

  @Autowired
  public void setMatchRepository(MatchRepository matchRepository) {
    this.matchRepository = matchRepository;
  }

  @Autowired
  public void setClubAdminRepository(ClubAdminRepository clubAdminRepository) {
    this.clubAdminRepository = clubAdminRepository;
  }

  @Autowired
  public void setTicketOfferRepository(TicketOfferRepository ticketOfferRepository) {
    this.ticketOfferRepository = ticketOfferRepository;
  }
}
