package fpc.app.service.app.impl;

import fpc.app.model.app.Club;
import fpc.app.repository.app.ClubRepository;
import fpc.app.service.app.ClubService;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Service;

@Service
public class ClubServiceImpl implements ClubService {
  private final ClubRepository clubRepository;

  public ClubServiceImpl(ClubRepository clubRepository) {
    this.clubRepository = clubRepository;
  }

  @Override
  @Nullable
  public Club getClub(Long clubId) {
    return clubRepository.findById(clubId).orElse(null);
  }
}
