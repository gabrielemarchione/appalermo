package gabrielemarchione.appalermo.repositories;

import gabrielemarchione.appalermo.entities.RuoloUtente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RuoloUtenteRepository extends JpaRepository<RuoloUtente, UUID> {
}
