package fpc.app.service.auth.impl;

import static java.util.Objects.isNull;

import fpc.app.dto.app.RegisterUserRequest;
import fpc.app.model.app.IdentityDocument;
import fpc.app.model.auth.User;
import fpc.app.repository.app.IdentityDocumentRepository;
import fpc.app.security.JwtUtil;
import fpc.app.service.auth.UserService;
import jakarta.validation.Valid;
import java.util.List;
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
  private final IdentityDocumentRepository identityDocumentRepository;

  public String login(String username, String password) {
    User user = userService.getByUsername(username);
    if (isNull(user) || !encoder.matches(password, user.getPassword()))
      throw new BadCredentialsException("Error: Incorrect credentials");
    return jwtUtil.generateToken(user);
  }

  public String register(@Valid RegisterUserRequest request) {
    User user = userService.save(request);
    return jwtUtil.generateToken(user);
  }

  public List<IdentityDocument> getIdentityDocumentTypes() {
    return identityDocumentRepository.findAll();
  }
}
