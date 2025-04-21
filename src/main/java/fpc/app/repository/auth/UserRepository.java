package fpc.app.repository.auth;

import fpc.app.model.auth.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);

  @Query(
      "select u from User u where u.person.name like concat('%', :keyword, '%') or u.person.lastName like concat('%', :keyword, '%') or u.username like concat('%', :keyword, '%')")
  List<User> findByKeyword(String keyword);
}
