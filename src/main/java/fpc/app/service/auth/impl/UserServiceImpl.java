package fpc.app.service.auth.impl;

import fpc.app.dto.app.RegisterUserRequest;
import fpc.app.exception.DataNotFoundException;
import fpc.app.exception.ValidationException;
import fpc.app.model.app.IdentityDocument;
import fpc.app.model.app.Person;
import fpc.app.model.auth.Role;
import fpc.app.model.auth.User;
import fpc.app.repository.app.PersonRepository;
import fpc.app.repository.auth.UserRepository;
import fpc.app.security.JwtUtil;
import fpc.app.service.auth.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
	private final PasswordEncoder encoder;
	private final UserRepository userRepository;
	private final PersonRepository personRepository;
	private final JwtUtil jwtUtil;

	@Override
	@Transactional
	public String registerUser(RegisterUserRequest request) {
		validateUserRegistrationRequest(request);
		Person person = personRepository.save(mapRequestToPerson(request));
		userRepository.save(
			User.builder()
				.person(person)
				.username(request.getEmail())
				.password(encoder.encode(request.getPassword()))
				.roles(List.of(new Role(1L)))
				.build());
		return jwtUtil.generateToken(person.getDocumentNumber());
	}

	private static Person mapRequestToPerson(RegisterUserRequest request) {
		return Person.builder()
			.name(request.getName())
			.lastName(request.getLastName())
			.birthday(request.getBirthDate())
			.gender(request.getGender()).documentType(new IdentityDocument(request.getDocumentTypeId()))
			.documentNumber(request.getDocumentNumber())
			.phone(request.getPhoneNumber())
			.email(request.getEmail())
			.build();
	}

	private void validateUserRegistrationRequest(RegisterUserRequest request) {
		validateEmail(request.getEmail());
		validateDocument(request);
	}

	private void validateDocument(RegisterUserRequest request) {
		if (personRepository.existsByDocumentTypeIdAndDocumentNumber(request.getDocumentTypeId(), request.getDocumentNumber()))
			throw new ValidationException("Error: Document is already in use");
	}

	private void validateEmail(String email) {
		if (personRepository.existsByEmail(email))
			throw new ValidationException("Error: Email is already in use");
	}

	@Override
	public UserDetails loadUser(String email) {
		User user = getByEmail(email);
		return new org.springframework.security.core.userdetails.User(
			user.getUsername(), user.getPassword(), new ArrayList<>());
	}

	@Override
	public User getByEmail(String email) {
		return userRepository
			.findByUsername(email)
			.orElseThrow(() -> new DataNotFoundException("Error: User not found"));
	}
}
