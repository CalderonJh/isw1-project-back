package fpc.app.service.auth.impl;

import static fpc.app.util.Tools.*;

import fpc.app.dto.user.UpdateUserDTO;
import fpc.app.dto.user.UserDTO;
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
  public User save(UserDTO request) {
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

  private Person savePersonInfo(UserDTO request) {
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

  private void validateUserRegistrationRequest(UserDTO request) {
    validateEmail(request.getEmail());
    validateDocument(request);
  }

  private void validateDocument(UserDTO request) {
    if (personRepository.existsByDocumentTypeIdAndDocumentNumber(
        request.getDocumentTypeId(), request.getDocumentNumber()))
      throw new ValidationException("Ya hay un usuario registrado con ese documento");
  }

  private void validateEmail(String email) {
    if (personRepository.existsByEmail(email))
      throw new ValidationException("Ya hay un usuario registrado con ese correo");
  }

  @Override
  public UserDetails loadUser(String email) {
    User user = required(getByUsername(email));
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

  @Override
  @Transactional
  public void updateUserInfo(User user, UpdateUserDTO update) {
    Person person = user.getPerson();
    person.setName(update.name());
    person.setLastName(update.lastName());
    person.setPhone(update.phoneNumber());
    if (!equalsText(user.getUsername(), update.email())) {
      validateEmail(update.email());
      person.setEmail(update.email());
      user.setUsername(update.email());
    }
    userRepository.save(user);
  }

  @Override
  public void updatePassword(User user, String newPassword) {
    user.setPassword(encoder.encode(newPassword));
    userRepository.save(user);
  }
}
