package fpc.app.service.auth.impl;

import fpc.app.model.auth.User;
import fpc.app.security.JwtUtil;
import fpc.app.service.auth.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserService userService;
  private final JwtUtil jwtUtil;
  private final PasswordEncoder encoder;

  public String authenticate(String email, String password) {
    User user = userService.getByEmail(email);
    if (!encoder.matches(password, user.getPassword()))
      throw new BadCredentialsException("Error: Incorrect Password");

    return jwtUtil.generateToken(user.getUsername());
  }
}
