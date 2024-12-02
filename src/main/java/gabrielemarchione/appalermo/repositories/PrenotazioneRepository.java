package gabrielemarchione.appalermo.repositories;

import gabrielemarchione.appalermo.entities.Evento;
import gabrielemarchione.appalermo.entities.Prenotazione;
import gabrielemarchione.appalermo.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione, UUID> {
    List<Prenotazione> findByUtente(Utente utente);

    @Query("SELECT COUNT(p.postiPrenotati) FROM Prenotazione p WHERE p.evento = :evento")
    int controllaPostiOccupatiByEvento(Evento evento);
}
