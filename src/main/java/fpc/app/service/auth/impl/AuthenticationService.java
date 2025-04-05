package fpc.app.service.auth.impl;

import fpc.app.dto.app.RegisterUserRequest;
import fpc.app.model.auth.User;
import fpc.app.security.JwtUtil;
import fpc.app.service.auth.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserService userService;
  private final JwtUtil jwtUtil;
  private final PasswordEncoder encoder;

  public String login(String username, String password) {
    User user = userService.getByUsername(username);
    if (isNull(user) || !encoder.matches(password, user.getPassword()))
      throw new BadCredentialsException("Error: Incorrect credentials");
    return jwtUtil.generateToken(user.getUsername());
  }

  public String register(@Valid RegisterUserRequest request) {
    User user = userService.save(request);
    return jwtUtil.generateToken(user.getUsername());
  }
}
