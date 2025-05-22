package fpc.app.service.app.impl;

import static fpc.app.util.Tools.*;

import fpc.app.constant.OfferStatus;
import fpc.app.dto.request.CreateTicketOfferDTO;
import fpc.app.dto.request.StandPriceDTO;
import fpc.app.dto.util.DateRange;
import fpc.app.exception.DataNotFoundException;
import fpc.app.exception.ValidationException;
import fpc.app.model.app.*;
import fpc.app.model.auth.User;
import fpc.app.repository.app.TicketOfferRepository;
import fpc.app.repository.app.TicketPurchaseRepository;
import fpc.app.repository.app.TicketTypeRepository;
import fpc.app.service.app.ClubAdminService;
import fpc.app.service.app.MatchService;
import fpc.app.service.app.TicketService;
import fpc.app.service.util.CloudinaryService;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
  private final TicketOfferRepository ticketOfferRepository;
  private final MatchService matchService;
  private final CloudinaryService cloudinaryService;
  private final ClubAdminService clubAdminService;
  private final TicketTypeRepository ticketTypeRepository;
  private final TicketPurchaseRepository ticketPurchaseRepository;

  @Override
  @Transactional
  public void createTicketOffer(
      User creator, Long matchId, CreateTicketOfferDTO dto, MultipartFile file) {
    ClubAdmin clubAdmin = clubAdminService.getClubAdmin(creator);
    String imageId = cloudinaryService.uploadImage(file);
    Match match = matchService.getMatch(matchId);
    validateMatch(match);
    LocalDateTime saleStartDate = validateSaleStartDate(dto.saleStartDate(), match);
    LocalDateTime saleEndDate = validateSaleEndDate(saleStartDate, dto.saleEndDate(), match);
    TicketOffer saved =
        save(
            TicketOffer.builder()
                .match(match)
                .publisher(clubAdmin)
                .startDate(saleStartDate)
                .endDate(saleEndDate)
                .imageId(imageId)
                .build());
    createTicketOfferTypes(saved.getId(), dto.standPrices());
  }

  private void createTicketOfferTypes(
      Long ticketOfferId, @Size(min = 1) List<StandPriceDTO> standPriceDTOS) {
    standPriceDTOS.forEach(
        standPrice ->
            ticketTypeRepository.save(
                TicketType.builder()
                    .ticketOfferId(ticketOfferId)
                    .stand(new Stand(standPrice.standId()))
                    .price(standPrice.price())
                    .build()));
  }

  private void validateMatch(Match match) {
    if (match.getStartDate() == null)
      throw new ValidationException("No se ha definido la fecha del partido");
    validateIsFutureDate(match.getStartDate(), "La fecha del partido debe ser futura");
    if (ticketOfferRepository.existsMatchTicketOffer(match.getId()))
      throw new ValidationException("Ya existe una oferta de tickets activa para este partido");
  }

  private LocalDateTime validateSaleStartDate(LocalDateTime startDate, Match match) {
    if (startDate == null) return getColTime();
    validateIsFutureDate(startDate);
    if (startDate.isAfter(match.getStartDate()))
      throw new ValidationException(
          "La fecha de inicio de venta debe ser antes del inicio del partido");

    return startDate;
  }

  /** Start date should already have been validated */
  private LocalDateTime validateSaleEndDate(
      LocalDateTime startDate, LocalDateTime endDate, Match match) {
    if (endDate == null) return match.getStartDate();
    validateDateRange(startDate, endDate);
    if (endDate.isAfter(match.getStartDate()))
      throw new ValidationException(
          "La fecha de cierre de venta debe ser antes del inicio del partido");

    return endDate;
  }

  public TicketOffer save(TicketOffer ticketOffer) {
    return ticketOfferRepository.save(ticketOffer);
  }

  @Override
  public OfferStatus toggleTicketOfferStatus(Long ticketOfferId) {
    TicketOffer offer = this.getTicketOffer(ticketOfferId);
    offer.setPaused(!offer.isPaused());
    ticketOfferRepository.save(offer);
    return OfferStatus.get(offer.isPaused());
  }

  public TicketOffer getTicketOffer(Long ticketOfferId) {
    return ticketOfferRepository.findById(ticketOfferId).orElseThrow(DataNotFoundException::new);
  }

  @Override
  public List<TicketOffer> getActiveOffers(List<Long> clubIds, boolean getAll) {
    return getAll
        ? ticketOfferRepository.getAllActive(getColTime())
        : ticketOfferRepository.getOffersByClubIdIn(clubIds, getColTime());
  }

  @Override
  public List<TicketOffer> getAllClubOffers(Club club) {
    return ticketOfferRepository.findByClubId(club.getId());
  }

  @Override
  public List<TicketType> getTicketOfferTypes(Long ticketId) {
    if (!ticketOfferRepository.existsById(ticketId))
      throw new DataNotFoundException("No se encontró la oferta de tickets");
    var types = ticketTypeRepository.findByTicketOfferId(ticketId);
    setStandAvailability(types);
    return types;
  }

  private void setStandAvailability(List<TicketType> types) {
    for (TicketType type : types) {
      Boolean available = ticketTypeRepository.isAvailable(type.getId());
      type.setAvailable(Boolean.TRUE.equals(available));
    }
  }

  @Override
  @Transactional
  public void updateTicketOfferImage(Long offerId, MultipartFile image) {
    TicketOffer offer = this.getTicketOffer(offerId);
    if (offer.getImageId() != null) cloudinaryService.deleteImage(offer.getImageId());

    String imageId = cloudinaryService.uploadImage(image);
    offer.setImageId(imageId);
    ticketOfferRepository.save(offer);
  }

  @Override
  public void updateTicketOfferDates(Long offerId, DateRange dateRange) {
    TicketOffer offer = this.getTicketOffer(offerId);
    if (!offer.getStartDate().equals(dateRange.start())) {
      validateSaleStartDate(dateRange.start(), offer.getMatch());
      offer.setStartDate(dateRange.start());
    }
    if (!offer.getEndDate().equals(dateRange.end())) {
      validateSaleEndDate(dateRange.start(), dateRange.end(), offer.getMatch());
      offer.setEndDate(dateRange.end());
    }
    ticketOfferRepository.save(offer);
  }

  @Override
  @Transactional
  public void updateTicketOfferPrice(Long offerId, Set<StandPriceDTO> prices) {
    for (StandPriceDTO standPrice : prices) {
      TicketType ticketType =
          ticketTypeRepository
              .findByTicketOfferIdAndStandId(offerId, standPrice.standId())
              .orElse(
                  TicketType.builder()
                      .ticketOfferId(offerId)
                      .stand(new Stand(standPrice.standId()))
                      .build());
      ticketType.setPrice(standPrice.price());
      ticketTypeRepository.save(ticketType);
    }
  }

  @Override
  public void purchase(Long ticketTypeId, User buyer) {
    TicketType type = ticketTypeRepository.findById(ticketTypeId).orElseThrow(DataNotFoundException::new);
    getTicketOffer(type.getTicketOfferId()).validateForSale();
    ticketPurchaseRepository.save(TicketPurchase.builder().ticketType(type).buyer(buyer).build());
  }

}
