package fpc.app.component;

import static java.lang.Long.parseLong;

import fpc.app.model.app.*;
import fpc.app.repository.app.TicketOfferRepository;
import fpc.app.repository.app.ClubAdminRepository;
import fpc.app.repository.app.MatchRepository;
import fpc.app.repository.app.StadiumRepository;
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
  private StadiumRepository stadiumRepository;

  @Override
  public boolean hasPermission(
      Authentication authentication, Object targetDomainObject, Object permission) {
    if (authentication == null || targetDomainObject == null) {
      return false;
    }

    Long userId = parseLong(authentication.getName());
    ClubAdmin admin = clubAdminRepository.findByUser(userId).orElse(null);

    if (admin == null) {
      return false;
    }

    Long adminClubId = admin.getClub().getId();

    // Verificar según el tipo de objeto
    return switch (targetDomainObject) {
      case Club club -> adminClubId.equals((club).getId());
      case TicketOffer offer -> adminClubId.equals((offer).getPublisher().getClub().getId());
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

    Long userId = parseLong(authentication.getName());
    ClubAdmin admin = clubAdminRepository.findByUser(userId).orElse(null);

    if (admin == null) {
      return false;
    }

    Long clubId = admin.getClub().getId();

    Long recordId = Long.valueOf(targetId.toString());
    return switch (targetType) {
      case "Club" -> clubId.equals(recordId);
      case "Match" -> clubId.equals(matchRepository.getHomeClubId(recordId));
      case "Stadium" -> clubId.equals(stadiumRepository.getClubId(recordId));
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

  @Autowired
  public void setStadiumRepository(StadiumRepository stadiumRepository) {
    this.stadiumRepository = stadiumRepository;
  }
}
