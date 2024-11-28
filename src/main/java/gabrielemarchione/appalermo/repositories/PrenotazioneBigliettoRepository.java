package gabrielemarchione.appalermo.repositories;

import gabrielemarchione.appalermo.entities.PrenotazioneBiglietto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PrenotazioneBigliettoRepository extends JpaRepository<PrenotazioneBiglietto, UUID> {
}
