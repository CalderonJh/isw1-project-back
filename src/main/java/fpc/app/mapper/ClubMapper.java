package fpc.app.mapper;

import fpc.app.dto.app.ClubDTO;
import fpc.app.model.app.Club;
import java.util.List;

public class ClubMapper {
  private ClubMapper() {}

  public static ClubDTO map(Club club) {
    return ClubDTO.builder()
        .id(club.getId())
        .name(club.getName())
        .shortName(club.getShortName())
        .imageId(club.getImageId())
        .build();
  }

  public static List<ClubDTO> map(List<Club> clubs) {
    return clubs.stream().map(ClubMapper::map).toList();
  }
}
