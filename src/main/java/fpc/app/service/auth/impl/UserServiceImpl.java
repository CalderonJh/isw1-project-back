package fpc.app.service.auth.impl;

import static fpc.app.util.Tools.hasText;
import static java.util.Objects.requireNonNull;

import fpc.app.dto.app.RegisterUserRequest;
import fpc.app.exception.ValidationException;
import fpc.app.model.app.IdentityDocument;
import fpc.app.model.app.Person;
import fpc.app.model.auth.Role;
import fpc.app.model.auth.User;
import fpc.app.repository.app.PersonRepository;
import fpc.app.repository.auth.RoleRepository;
import fpc.app.repository.auth.UserRepository;
import fpc.app.service.auth.UserService;
import jakarta.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
  private final PasswordEncoder encoder;
  private final UserRepository userRepository;
  private final PersonRepository personRepository;
  private final RoleRepository roleRepository;

  @Override
  @Transactional
  public User save(RegisterUserRequest request) {
    validateUserRegistrationRequest(request);
    Person person = savePersonInfo(request);
    return userRepository.save(
        User.builder()
            .person(person)
            .username(request.getEmail())
            .password(encoder.encode(request.getPassword()))
            .roles(Set.of(roleRepository.findByName("USER").orElseThrow()))
            .build());
  }

  private Person savePersonInfo(RegisterUserRequest request) {
    return personRepository.save(
        Person.builder()
            .name(request.getName())
            .lastName(request.getLastName())
            .birthday(request.getBirthDate())
            .gender(request.getGender())
            .documentType(new IdentityDocument(request.getDocumentTypeId()))
            .documentNumber(request.getDocumentNumber())
            .phone(request.getPhoneNumber())
            .email(request.getEmail())
            .build());
  }

  private void validateUserRegistrationRequest(RegisterUserRequest request) {
    validateEmail(request.getEmail());
    validateDocument(request);
  }

  private void validateDocument(RegisterUserRequest request) {
    if (personRepository.existsByDocumentTypeIdAndDocumentNumber(
        request.getDocumentTypeId(), request.getDocumentNumber()))
      throw new ValidationException("Error: Document is already in use");
  }

  private void validateEmail(String email) {
    if (personRepository.existsByEmail(email))
      throw new ValidationException("Error: Email is already in use");
  }

  @Override
  public UserDetails loadUser(String email) {
    User user = requireNonNull(getByUsername(email));
    return new org.springframework.security.core.userdetails.User(
        user.getUsername(), user.getPassword(), new ArrayList<>());
  }

  @Override
  public @Nullable User getByUsername(String username) {
    return userRepository.findByUsername(username).orElse(null);
  }

  @Override
  public List<User> list(String keyword) {
    return hasText(keyword) ? userRepository.findAll() : userRepository.findByKeyword(keyword);
  }

  @Override
  public List<Role> listRoles() {
    return roleRepository.findAll();
  }
}
