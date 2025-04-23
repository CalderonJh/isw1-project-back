package fpc.app.service.app;

import fpc.app.model.app.Club;
import jakarta.annotation.Nullable;

public interface ClubService {
  @Nullable
  Club getClub(Long clubId);
}
