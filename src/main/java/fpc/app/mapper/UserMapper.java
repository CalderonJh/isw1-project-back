package fpc.app.mapper;

import fpc.app.dto.UserDTO;
import fpc.app.model.auth.User;

public class UserMapper {
  private UserMapper() {}

  public static UserDTO map(User user) {
    return UserDTO.builder()
        .name(user.getPerson().getName())
        .lastName(user.getPerson().getLastName())
        .email(user.getUsername())
        .documentTypeId(user.getPerson().getDocumentType().getId())
        .documentNumber(user.getPerson().getDocumentNumber())
        .gender(user.getPerson().getGender())
        .birthDate(user.getPerson().getBirthday())
        .phoneNumber(user.getPerson().getPhone())
        .build();
  }
}
