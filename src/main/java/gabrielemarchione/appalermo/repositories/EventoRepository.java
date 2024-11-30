package gabrielemarchione.appalermo.repositories;

import gabrielemarchione.appalermo.entities.Evento;
import gabrielemarchione.appalermo.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EventoRepository extends JpaRepository<Evento, UUID> {
    List<Evento> findByOrganizzatore(Utente organizzatore);
}
