package fpc.app.service.auth.impl;

import static fpc.app.util.Tools.*;

import fpc.app.dto.UserDTO;
import fpc.app.dto.request.UpdateUserDTO;
import fpc.app.exception.DataNotFoundException;
import fpc.app.exception.ValidationException;
import fpc.app.model.app.IdentityDocument;
import fpc.app.model.app.Person;
import fpc.app.model.auth.Role;
import fpc.app.model.auth.User;
import fpc.app.repository.app.IdentityDocumentRepository;
import fpc.app.repository.app.PersonRepository;
import fpc.app.repository.auth.RoleRepository;
import fpc.app.repository.auth.UserRepository;
import fpc.app.service.auth.UserService;
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
  private final IdentityDocumentRepository identityDocumentRepository;

  @Override
  @Transactional
  public User save(UserDTO request) {
    validateUserRegistrationRequest(request);
    Person person = savePersonInfo(request);
    return userRepository.save(
        User.builder()
            .person(person)
            .username(request.email())
            .password(encoder.encode(request.password()))
            .roles(Set.of(roleRepository.findByName("USER").orElseThrow()))
            .build());
  }

  private Person savePersonInfo(UserDTO request) {
    return personRepository.save(
        Person.builder()
            .name(request.name())
            .lastName(request.lastName())
            .birthday(request.birthDate())
            .gender(request.gender())
            .documentType(new IdentityDocument(request.documentTypeId()))
            .documentNumber(request.documentNumber())
            .phone(request.phoneNumber())
            .email(request.email())
            .build());
  }

  private void validateUserRegistrationRequest(UserDTO request) {
    validateEmail(request.email());
    validateDocument(request);
  }

  private void validateDocument(UserDTO request) {
    if (personRepository.existsByDocumentTypeIdAndDocumentNumber(
        request.documentTypeId(), request.documentNumber()))
      throw new ValidationException("Ya hay un usuario registrado con ese documento");
  }

  private void validateEmail(String email) {
    if (personRepository.existsByEmail(email))
      throw new ValidationException("Ya hay un usuario registrado con ese correo");
  }

  @Override
  public UserDetails loadUser(Long userId) {
    User user = getUser(userId);
    return new org.springframework.security.core.userdetails.User(
        user.getId().toString(), user.getPassword(), new ArrayList<>());
  }

  @Override
  public User getUser(Long userID) {
    return userRepository
        .findById(userID)
        .orElseThrow(() -> new DataNotFoundException("User not found with given ID"));
  }

  @Override
  public User getUser(String username) {
    return userRepository
        .findByUsername(username)
        .orElseThrow(() -> new DataNotFoundException("User not found with given username"));
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

  @Override
  public List<IdentityDocument> getIdentityDocumentTypes() {
    return identityDocumentRepository.findAll();
  }
}
