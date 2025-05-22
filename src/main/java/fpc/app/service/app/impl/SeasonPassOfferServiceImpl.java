package fpc.app.service.app.impl;

import static fpc.app.util.Tools.getColTime;
import static fpc.app.util.Tools.isFutureDate;
import static java.util.Objects.isNull;

import fpc.app.constant.OfferStatusType;
import fpc.app.dto.request.CreateSeasonPassOfferDTO;
import fpc.app.dto.request.StandPriceDTO;
import fpc.app.dto.response.SeasonPassOfferDetailDTO;
import fpc.app.dto.util.DateRange;
import fpc.app.exception.DataNotFoundException;
import fpc.app.exception.ValidationException;
import fpc.app.mapper.SeasonPassOfferMapper;
import fpc.app.model.app.*;
import fpc.app.model.auth.User;
import fpc.app.repository.app.*;
import fpc.app.service.app.ClubService;
import fpc.app.service.app.SeasonPassOfferService;
import fpc.app.service.app.StadiumService;
import fpc.app.service.util.CloudinaryService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class SeasonPassOfferServiceImpl implements SeasonPassOfferService {
  private final SeasonPassOfferRepository seasonPassOfferRepository;
  private final MatchRepository matchRepository;
  private final CloudinaryService cloudinaryService;
  private final SeasonPassTypeRepository seasonPassTypeRepository;
  private final SeasonPassHolderRepository seasonPassHolderRepository;
  private final ClubService clubService;
  private final StadiumService stadiumService;

  @Override
  @Transactional
  public void createSeasonPassOffer(
      Long clubAdminId, CreateSeasonPassOfferDTO dto, MultipartFile image) {
    Club club = clubService.getClubByAdminId(clubAdminId);
    List<Match> matches = matchRepository.findAllById(dto.matchIds());
    validateGames(club.getId(), matches);
    validateStands(dto.standPrices(), matches.getFirst().getStadium());
    String imageId = image == null ? null : cloudinaryService.uploadImage(image);
    SeasonPassOffer toSave =
        SeasonPassOffer.builder()
            .publisherId(clubAdminId)
            .club(club)
            .stadiumId(matches.getFirst().getStadium().getId())
            .description(dto.description())
            .season(dto.season())
            .year(dto.year())
            .startDate(dto.startDate())
            .endDate(dto.endDate())
            .imageId(imageId)
            .matches(matches)
            .build();
    toSave.validateDateRange();
    SeasonPassOffer saved = seasonPassOfferRepository.save(toSave);

    dto.standPrices()
        .forEach(
            sp ->
                seasonPassTypeRepository.save(
                    SeasonPassType.builder()
                        .seasonPassOfferId(saved.getId())
                        .price(sp.price())
                        .stand(new Stand(sp.standId()))
                        .build()));
  }

  private void validateStands(Set<StandPriceDTO> standPriceDTOS, Stadium stadium) {
    Set<Long> stadiumStandsIds =
        stadium.getStands().stream().map(Stand::getId).collect(Collectors.toSet());
    Set<Long> givenStandsIds =
        standPriceDTOS.stream().map(StandPriceDTO::standId).collect(Collectors.toSet());
    if (!stadiumStandsIds.containsAll(givenStandsIds))
      throw new ValidationException("Se seleccionaron tribunas que no pertenecen al estadio");
  }

  private void validateGames(Long clubId, List<Match> matches) {
    Set<Long> matchesHomeClubId =
        matches.stream().map(m -> m.getHomeClub().getId()).collect(Collectors.toSet());
    if (matchesHomeClubId.size() != 1 || !clubId.equals(matchesHomeClubId.iterator().next()))
      throw new ValidationException("Se seleccionaron partidos que no pertenecen al club");

    Set<Long> matchesStadiumId =
        matches.stream().map(m -> m.getStadium().getId()).collect(Collectors.toSet());
    if (matchesStadiumId.size() != 1)
      throw new ValidationException("Todos los partidos deben ser en el mismo estadio");

    boolean containsPastGames =
        matches.stream()
            .anyMatch(m -> !isNull(m.getStartDate()) && !isFutureDate(m.getStartDate()));
    if (containsPastGames) throw new ValidationException("Algunos partidos tienen fecha pasada");
  }

  @Override
  public List<SeasonPassOffer> getSeasonPassOffers(List<Long> clubIds, boolean getAll) {
    if (getAll) return seasonPassOfferRepository.findAllActive(getColTime());
    else return seasonPassOfferRepository.findAllActiveByClubIdIn(clubIds, getColTime());
  }

  @Override
  public SeasonPassOfferDetailDTO getSeasonPassTypes(Long seasonPassOfferId) {
    SeasonPassOffer offer = getSeasonPassOffer(seasonPassOfferId);
    List<SeasonPassType> types =  seasonPassTypeRepository.getSeasonPassTypes(offer.getId());
    setStandAvailability(types);
    return SeasonPassOfferMapper.toDetailDTO(types, offer);
  }

  private void setStandAvailability(List<SeasonPassType> types) {
    for (SeasonPassType type : types) {
      Boolean available = seasonPassTypeRepository.isAvailable(type.getId());
      type.setAvailable(Boolean.TRUE.equals(available));
    }
  }

  @Override
  public SeasonPassOffer getSeasonPassOffer(Long seasonPassOfferId) {
    return seasonPassOfferRepository
        .findById(seasonPassOfferId)
        .orElseThrow(() -> new DataNotFoundException("No se encontró la oferta de abono"));
  }

  @Override
  public void purchase(Long seasonPassTypeId, User buyer) {
    SeasonPassType type = seasonPassTypeRepository.findById(seasonPassTypeId).orElseThrow(DataNotFoundException::new);
    this.getSeasonPassOffer(type.getSeasonPassOfferId()).validateForSale();
    seasonPassHolderRepository.save(
        SeasonPassHolder.builder().seasonPassType(type).user(buyer).build());
  }

  @Override
  public List<SeasonPassOffer> getAllSeasonPassOffers(Club club) {
    return seasonPassOfferRepository.findAllByClubId(club.getId());
  }

  @Override
  public OfferStatusType toggleSeasonPassOfferStatus(Long offerId) {
    SeasonPassOffer offer = getSeasonPassOffer(offerId);
    offer.setPaused(!offer.isPaused());
    seasonPassOfferRepository.save(offer);
    return OfferStatusType.get(offer.isPaused());
  }

  @Override
  @Transactional
  public void updateSeasonPassOfferImage(Long id, MultipartFile file) {
    SeasonPassOffer offer = getSeasonPassOffer(id);
    if (offer.getImageId() != null) cloudinaryService.deleteImage(offer.getImageId());
    String imageId = cloudinaryService.uploadImage(file);
    offer.setImageId(imageId);
    seasonPassOfferRepository.save(offer);
  }

  @Override
  public void updateDates(Long offerId, DateRange dateRange) {
    SeasonPassOffer offer = getSeasonPassOffer(offerId);
    dateRange.validate();
    if (!offer.getStartDate().equals(dateRange.start())) {
      offer.setStartDate(dateRange.start());
      offer.validateDateRange();
    }
    if (!offer.getEndDate().equals(dateRange.end())) {
      offer.setStartDate(dateRange.start());
      offer.validateEndDate();
    }
    seasonPassOfferRepository.save(offer);
  }

  @Override
  @Transactional
  public void updateSeasonPassOfferPrice(Long offerId, Set<StandPriceDTO> prices) {
    SeasonPassOffer offer = getSeasonPassOffer(offerId);
    for (StandPriceDTO price : prices) {
      SeasonPassType seasonPassType =
          seasonPassTypeRepository
              .findBySeasonPassOfferIdAndStandId(offerId, price.standId())
              .orElse(null);
      if (seasonPassType != null) {
      seasonPassType.setPrice(price.price());
        seasonPassTypeRepository.save(seasonPassType);
      } else createNewSeasonPassType(offer, price.standId(), price.price());
    }
  }

  public void createNewSeasonPassType(SeasonPassOffer offer, Long standId, BigDecimal price) {
    List<Stand> stands = stadiumService.getStadium(offer.getStadiumId()).getStands();
    if (stands.stream().noneMatch(s -> s.getId().equals(standId)))
      throw new ValidationException("La tribuna no pertenece al estadio");

    seasonPassTypeRepository.save(
        SeasonPassType.builder()
            .seasonPassOfferId(offer.getId())
            .stand(new Stand(standId))
            .price(price)
            .build());
  }
}
