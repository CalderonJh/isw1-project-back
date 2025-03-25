package fpc.app.service.auth;

import fpc.app.model.auth.User;
import org.springframework.security.core.userdetails.UserDetails;
import fpc.app.dto.app.RegisterUserRequest;
public interface UserService {

  User getByEmail(String email);

  UserDetails loadUser(String email);

  String registerUser(RegisterUserRequest request);
}
