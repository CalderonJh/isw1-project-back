package fpc.app.service.auth;

import fpc.app.model.auth.User;
import org.springframework.security.core.userdetails.UserDetails;
import fpc.app.dto.app.RegisterUserRequest;
public interface UserService {

  User getByUsername(String username);

  UserDetails loadUser(String email);

  User save(RegisterUserRequest request);
}
