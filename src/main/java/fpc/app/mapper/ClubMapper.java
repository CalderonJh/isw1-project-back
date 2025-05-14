package fpc.app.mapper;

import fpc.app.dto.response.ClubResponseDTO;
import fpc.app.model.app.Club;
import java.util.List;

public class ClubMapper {
  private ClubMapper() {}

  public static ClubResponseDTO map(Club club) {
    return ClubResponseDTO.builder()
        .id(club.getId())
        .name(club.getName())
        .shortName(club.getShortName())
        .imageId(club.getImageId())
        .build();
  }

  public static List<ClubResponseDTO> map(List<Club> clubs) {
    return clubs.stream().map(ClubMapper::map).toList();
  }
}
