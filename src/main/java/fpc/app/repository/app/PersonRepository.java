package fpc.app.repository.app;

import fpc.app.model.app.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
	boolean existsByDocumentTypeIdAndDocumentNumber(Long documentTypeId, String documentNumber);

	boolean existsByEmail(String email);
}
