package fpc.app.service.app.impl;

import static fpc.app.util.Tools.*;

import fpc.app.constant.OfferStatus;
import fpc.app.dto.app.CreateTicketOfferDTO;
import fpc.app.exception.ValidationException;
import fpc.app.model.app.ClubAdmin;
import fpc.app.model.app.Match;
import fpc.app.model.app.TicketOffer;
import fpc.app.model.auth.User;
import fpc.app.repository.TicketOfferRepository;
import fpc.app.service.app.ClubAdminService;
import fpc.app.service.app.ClubService;
import fpc.app.service.app.MatchService;
import fpc.app.service.app.TicketService;
import fpc.app.service.util.CloudinaryService;
import jakarta.annotation.Nullable;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
  private final TicketOfferRepository ticketOfferRepository;
  private final MatchService matchService;
  private final CloudinaryService cloudinaryService;
  private final ClubService clubService;
  private final ClubAdminService clubAdminService;

  @Override
  public void createTicketOffer() {}

  @Override
  public void createTicketOffer(User creator, CreateTicketOfferDTO dto, MultipartFile file) {
    ClubAdmin clubAdmin = clubAdminService.getClubAdmin(creator);
    String imageId = cloudinaryService.uploadImage(file);
    Match match = matchService.getMatch(dto.matchId());
    validateIsFutureDate(match.getStartDate().minusHours(1));
    LocalDateTime saleStartDate = validateSaleStartDate(dto.saleStartDate(), match);
    LocalDateTime saleEndDate = validateSaleEndDate(saleStartDate, dto.saleEndDate(), match);
    save(
        TicketOffer.builder()
            .match(match)
            .postedBy(clubAdmin)
            .startDate(saleStartDate)
            .endDate(saleEndDate)
            .imageId(imageId)
            .build());
  }

  private LocalDateTime validateSaleStartDate(LocalDateTime startDate, Match match) {
    if (startDate == null) return getColTime();
    validateIsFutureDate(startDate);
    if (!startDate.isBefore(match.getStartDate())) {
      throw new ValidationException(
          "La fecha de inicio de venta debe ser al menos una hora antes del inicio del partido");
    }
    return startDate;
  }

  /** Start date should already have been validated */
  private LocalDateTime validateSaleEndDate(
      LocalDateTime startDate, LocalDateTime endDate, Match match) {
    if (endDate == null) return match.getStartDate().minusMinutes(1);
    validateDateRange(startDate, endDate);
    return endDate;
  }

  public TicketOffer save(TicketOffer ticketOffer) {
    return ticketOfferRepository.save(ticketOffer);
  }

  public void changeTicketOfferStatus(Long ticketOfferId, OfferStatus status) {
    TicketOffer offer = required(get(ticketOfferId));
    if (OfferStatus.ENABLED.equals(status)) {
      enableTickeOffer(offer);
    } else if (OfferStatus.DISABLED.equals(status)) {
      disableTicketOffer(offer);
    }
  }

  private void disableTicketOffer(TicketOffer offer) {}

  private void enableTickeOffer(TicketOffer offer) {}

  public @Nullable TicketOffer get(Long ticketOfferId) {
    return ticketOfferRepository.findById(ticketOfferId).orElse(null);
  }
}
