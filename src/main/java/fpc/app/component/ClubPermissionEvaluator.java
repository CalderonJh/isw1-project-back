package fpc.app.component;

import static java.lang.Long.parseLong;

import fpc.app.model.app.*;
import fpc.app.repository.app.*;
import java.io.Serializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class ClubPermissionEvaluator implements PermissionEvaluator {
  private TicketOfferRepository ticketOfferRepository;
  private MatchRepository matchRepository;
  private StadiumRepository stadiumRepository;
  private SeasonPassOfferRepository seasonPassOfferRepository;
  private ClubRepository clubRepository;

  @Override
  public boolean hasPermission(
      Authentication authentication, Object targetDomainObject, Object permission) {
    if (authentication == null || targetDomainObject == null) {
      return false;
    }

    Long userId = parseLong(authentication.getName());

    // Verificar según el tipo de objeto
    return switch (targetDomainObject) {
      case Club club -> hasClubPermission(userId, club.getId());
      case TicketOffer offer -> hasClubPermission(userId, offer.getClubId());
      case Match match -> hasClubPermission(userId, match.getHomeClub().getId());
      default -> false;
    };
  }

  private boolean hasClubPermission(Long userId, Long club) {
    return clubRepository.existsPermission(userId, club);
  }

  @Override
  public boolean hasPermission(
      Authentication authentication, Serializable targetId, String targetType, Object permission) {
    // Implementación para casos donde solo tenemos IDs
    if (authentication == null || targetId == null || targetType == null) {
      return false;
    }

    Long userId = parseLong(authentication.getName());

    Long recordId = Long.valueOf(targetId.toString());
    return switch (targetType) {
      case "Club" -> hasClubPermission(userId, recordId);
      case "Match" -> hasMatchPermission(userId, recordId);
      case "Stadium" -> hasStadiumPermission(userId, recordId);
      case "TicketOffer" -> hasTicketOfferPermission(userId, recordId);
      case "SeasonPassOffer" -> hasSeasonPassOfferPermission(userId, recordId);
      default -> false;
    };
  }

  private boolean hasSeasonPassOfferPermission(Long userId, Long recordId) {
    Long clubId = seasonPassOfferRepository.getClubId(recordId);
    return hasClubPermission(userId, clubId);
  }

  private boolean hasTicketOfferPermission(Long userId, Long offerId) {
    Long clubId = ticketOfferRepository.getClubId(offerId);
    return hasClubPermission(userId, clubId);
  }

  private boolean hasStadiumPermission(Long userId, Long stadiumId) {
    Long clubId = stadiumRepository.getClubId(stadiumId);
    return hasClubPermission(userId, clubId);
  }

  private boolean hasMatchPermission(Long userId, Long matchId) {
    Long clubId = matchRepository.getHomeClubId(matchId);
    return hasClubPermission(userId, clubId);
  }

  @Autowired
  public void setMatchRepository(MatchRepository matchRepository) {
    this.matchRepository = matchRepository;
  }

  @Autowired
  public void setTicketOfferRepository(TicketOfferRepository ticketOfferRepository) {
    this.ticketOfferRepository = ticketOfferRepository;
  }

  @Autowired
  public void setStadiumRepository(StadiumRepository stadiumRepository) {
    this.stadiumRepository = stadiumRepository;
  }

  @Autowired
  public void setSeasonPassOfferRepository(SeasonPassOfferRepository seasonPassOfferRepository) {
    this.seasonPassOfferRepository = seasonPassOfferRepository;
  }

  @Autowired
  public void setClubRepository(ClubRepository clubRepository) {
    this.clubRepository = clubRepository;
  }
}
