package fpc.app.service.auth;

import fpc.app.dto.app.RegisterUserRequest;
import fpc.app.model.auth.Role;
import fpc.app.model.auth.User;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

  User getByUsername(String username);

  UserDetails loadUser(String email);

  User save(RegisterUserRequest request);

  List<User> list(String keyword);

  List<Role> listRoles();
}
