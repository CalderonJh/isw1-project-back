package fpc.app.mapper;

import fpc.app.dto.response.ClubResponseDTO;
import fpc.app.model.app.Club;
import java.util.List;

public class ClubMapper {
  private ClubMapper() {}

  public static ClubResponseDTO map(Club club, boolean addAdminInfo) {
    return ClubResponseDTO.builder()
        .id(club.getId())
        .name(club.getName())
        .shortName(club.getShortName())
        .imageId(club.getImageId())
        .admin(club.getAdmin() != null && addAdminInfo ? club.getAdmin().getReference() : null)
        .build();
  }

  public static List<ClubResponseDTO> map(List<Club> clubs, boolean addAdminInfo) {
    return clubs.stream().map(club -> map(club, addAdminInfo)).toList();
  }
}
