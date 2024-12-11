package gabrielemarchione.appalermo.services;


import gabrielemarchione.appalermo.dto.PrenotazioneDTO;
import gabrielemarchione.appalermo.entities.Evento;
import gabrielemarchione.appalermo.entities.Prenotazione;
import gabrielemarchione.appalermo.entities.Utente;
import gabrielemarchione.appalermo.exceptions.AuthDeniedException;
import gabrielemarchione.appalermo.exceptions.BadRequestException;
import gabrielemarchione.appalermo.exceptions.NotFoundException;
import gabrielemarchione.appalermo.exceptions.UnauthorizedException;
import gabrielemarchione.appalermo.repositories.PrenotazioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PrenotazioneService {
    @Autowired
    private PrenotazioneRepository prenotazioneRepository;
    @Autowired
    private EventoService eventoService;

    public Page<Prenotazione> findAllPrenotazioni(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return prenotazioneRepository.findAll(pageable);
    }

    public Prenotazione findPrenotazioneById(UUID prenotazioneId) {
        return prenotazioneRepository.findById(prenotazioneId).orElseThrow(() -> new NotFoundException("prenotazione", prenotazioneId));
    }

    public Prenotazione savePrenotazione(PrenotazioneDTO body, Utente currentUtente) {
        Evento evento = eventoService.getEventoById(body.eventoId());
        int postiOccupati = prenotazioneRepository.controllaPostiOccupatiByEvento(evento);

        if (evento.getPostiMassimi() - postiOccupati < body.postiPrenotati())
            throw new BadRequestException("non ci sono abbastanza posti disponibili");
        if (evento.getOrganizzatore().getUtenteId().equals(currentUtente.getUtenteId()))
            throw new UnauthorizedException("l'organizzatore non puà fare una prenotazione per il suo evento");
        return prenotazioneRepository.save(new Prenotazione(body.postiPrenotati(), currentUtente, evento));
    }

    public List<Prenotazione> findPrenotazioneByUtente(Utente utente) {
        return prenotazioneRepository.findByUtente(utente);
    }

    public void cancellaPrenotazioneById(UUID prenotazioneId, Utente utente) {
        Prenotazione prenotazione = findPrenotazioneById(prenotazioneId);
        if (!prenotazione.getUtente().getUtenteId().equals(utente.getUtenteId()))
            throw new AuthDeniedException("Non sei autorizzato a cancellare questa prenotazione");
        prenotazioneRepository.delete(prenotazione);
    }


}
