package fpc.app.model.auth;

import fpc.app.model.app.Person;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user", schema = "app")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "person_id")
	private Person person;

	@NotBlank
	@Column(nullable = false, unique = true, length = 100)
	private String username;

	@NotBlank
	@Column(nullable = false)
	private String password;

  @ManyToMany
  @Builder.Default
  @JoinTable(
      name = "user_role",
      schema = "app",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  public User(Long id) {
    this.id = id;
  }
}
