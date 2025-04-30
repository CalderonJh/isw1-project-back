package fpc.app.mapper;

import fpc.app.dto.app.StadiumDTO;
import fpc.app.dto.app.StandDTO;
import fpc.app.model.app.Stadium;
import fpc.app.model.app.Stand;
import java.util.List;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StadiumMapper {
  StadiumMapper INSTANCE = Mappers.getMapper(StadiumMapper.class);

  @Mapping(source = "stands", target = "stands", qualifiedByName = "toStandDTOList")
  StadiumDTO toDTO(Stadium stadium);

  @Named("toStandDTO")
  StandDTO toStandDTO(Stand stand);

  @Named("toStandDTOList")
  @IterableMapping(qualifiedByName = "toStandDTO")
  List<StandDTO> toStandDTOList(List<Stand> stands);

  List<StadiumDTO> toDTO(List<Stadium> stadiums);
}
