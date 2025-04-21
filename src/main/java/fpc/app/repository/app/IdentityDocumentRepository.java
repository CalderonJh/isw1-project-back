package fpc.app.repository.app;

import fpc.app.model.app.IdentityDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdentityDocumentRepository extends JpaRepository<IdentityDocument, Long> {}
