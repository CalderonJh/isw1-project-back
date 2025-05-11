package fpc.app.mapper;

import fpc.app.dto.app.StadiumDTO;
import fpc.app.dto.app.StandDTO;
import fpc.app.model.app.Stadium;
import fpc.app.model.app.Stand;
import java.util.List;

public class StadiumMapper {
  private StadiumMapper() {}

  public static StadiumDTO map(Stadium stadium) {
    if (stadium == null) return null;
    return new StadiumDTO(
        stadium.getId(),
        stadium.getName(),
        stadium.getStands().stream().map(StadiumMapper::mapStand).toList());
  }

  public static List<StadiumDTO> map(List<Stadium> stadiums) {
    return stadiums.stream().map(StadiumMapper::map).toList();
  }

  public static StandDTO mapStand(Stand stand) {
    return new StandDTO(stand.getName(), stand.getCapacity());
  }
}
