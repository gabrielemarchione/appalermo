package gabrielemarchione.appalermo.repositories;

import gabrielemarchione.appalermo.entities.Biglietto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BigliettoRepository extends JpaRepository<Biglietto, UUID> {
}
