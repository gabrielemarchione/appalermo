package gabrielemarchione.appalermo.repositories;

import gabrielemarchione.appalermo.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UtenteRepository extends JpaRepository<Utente, UUID> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<Utente> findUtenteByUsername(String username);
}
