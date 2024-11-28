package gabrielemarchione.appalermo.repositories;

import gabrielemarchione.appalermo.entities.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventoRepository extends JpaRepository<Evento, UUID> {
}
