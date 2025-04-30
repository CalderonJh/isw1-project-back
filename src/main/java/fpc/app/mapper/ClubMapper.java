package fpc.app.mapper;

import fpc.app.dto.app.ClubDTO;
import fpc.app.model.app.Club;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClubMapper {
  ClubMapper INSTANCE = Mappers.getMapper(ClubMapper.class);

	ClubDTO toClubDTO(Club club);

}
