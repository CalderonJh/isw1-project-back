package fpc.app.model.app;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "person", schema = "app")
public class Person {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Column(name = "name", nullable = false)
	private String name;

	@NotBlank
	@Column(name = "last_name", nullable = false)
	private String lastName;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "doc_type_id", nullable = false)
	private IdentityDocument documentType;

	@NotBlank
	@Column(name = "doc_number", nullable = false, length = 25)
	private String documentNumber;

	@NotNull
	@Temporal(TemporalType.DATE)
	@Column(name = "birthday", nullable = false)
	private LocalDate birthday;

	@NotBlank
	@Column(name = "gender", nullable = false, length = 1)
	@Pattern(regexp = "[MF]")
	private String gender;

	@Email
	@NotBlank
	@Column(name = "email", nullable = false, unique = true, length = 250)
	private String email;

	@NotBlank
	@Column(name = "phone", nullable = false, length = 20)
	@Pattern(regexp = "\\d{7,20}")
	private String phone;
}
