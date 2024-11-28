package gabrielemarchione.appalermo.repositories;

import gabrielemarchione.appalermo.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UtenteRepository extends JpaRepository<Utente, UUID> {
}
