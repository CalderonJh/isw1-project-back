package fpc.app.service.auth;

import fpc.app.dto.app.UserDTO;
import fpc.app.dto.app.UpdateUserDTO;
import fpc.app.model.auth.Role;
import fpc.app.model.auth.User;
import java.util.List;

import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

  User getByUsername(String username);

  UserDetails loadUser(String email);

  User save(UserDTO request);

  List<User> list(String keyword);

  List<Role> listRoles();

	void updateUserInfo(User user, @Valid UpdateUserDTO update);

  void updatePassword(User user, String newPassword);
}
