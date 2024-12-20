package gabrielemarchione.appalermo.services;

import gabrielemarchione.appalermo.dto.EventoDTO;
import gabrielemarchione.appalermo.entities.Evento;
import gabrielemarchione.appalermo.entities.Utente;
import gabrielemarchione.appalermo.entities.enums.CategoriaEvento;
import gabrielemarchione.appalermo.exceptions.AuthDeniedException;
import gabrielemarchione.appalermo.exceptions.BadRequestException;
import gabrielemarchione.appalermo.exceptions.NotFoundException;
import gabrielemarchione.appalermo.repositories.EventoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EventoService {
    @Autowired
    private EventoRepository eventoRepository;
    @Autowired
    private List<DateTimeFormatter> formatters;
    @Autowired
    private UnsplashService unsplashService;

    public Page<Evento> findAllEvento(String titolo, LocalDate data, CategoriaEvento categoriaEvento, Double costo, String organizzatore, int page, int size, String sortBy) {
        System.out.println("Data ricevuta nel service: " + data);


        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return eventoRepository.findFilteredEvents(titolo, data, categoriaEvento, costo, organizzatore, pageable);
    }


    public Evento getEventoById(UUID eventoId) {
        return eventoRepository.findById(eventoId).orElseThrow(() -> new NotFoundException("evento non trovato", eventoId));
    }
    private LocalDate validateDate(String data) {
        for (DateTimeFormatter formatter : formatters) {
            try {
                LocalDate dateFormatted = LocalDate.parse(data, formatter);
                if (dateFormatted.isBefore(LocalDate.now()))
                    throw new BadRequestException("La data deve essere futura");
                return dateFormatted;
            } catch (DateTimeParseException ignored) {

            }
        }
        throw new BadRequestException("Format date not supported");
    }
    public Evento saveEvento(EventoDTO body, Utente organizzatore) {
        LocalDate data = validateDate(body.data());

        // Genera l'immagine se non fornita
        String immagine = body.immagine();
        if (immagine == null || immagine.isBlank()) {
            immagine = unsplashService.getImageByCategory(body.categoriaEvento());
        }

        Evento nuovoEvento = new Evento(
                body.titolo(),
                body.descrizione(),
                data,
                body.orarioInizio(),
                body.luogo(),
                body.costo(),
                body.postiMassimi(),
                body.postiDisponibili(),
                body.categoriaEvento(),
                immagine,
                organizzatore

        );

        return eventoRepository.save(nuovoEvento);
    }


    public List<Evento> findAllEventoByOrganizzatore(Utente organizzatore) {
        if (organizzatore.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN"))){
            return eventoRepository.findAll();
        }
        return eventoRepository.findByOrganizzatore(organizzatore);
    }

    public Evento findEventoByIdAndUpdate(EventoDTO body, UUID eventoId, Utente organizzatore) {
        Evento cercato = getEventoById(eventoId);

        boolean isAdmin = organizzatore.getRuoli().stream()
                .anyMatch(ruolo -> ruolo.getNome().equals("ADMIN"));

        if (!isAdmin && !cercato.getOrganizzatore().getUtenteId().equals(organizzatore.getUtenteId())) {
            throw new AuthDeniedException("Non sei autorizzato a modificare l'evento");
        }

        cercato.setData(validateDate(body.data()));
        cercato.setDescrizione(body.descrizione());
        cercato.setTitolo(body.titolo());
        cercato.setLuogo(body.luogo());
        cercato.setPostiMassimi(body.postiMassimi());
        return eventoRepository.save(cercato);
    }
    public void deleteEvento(UUID eventoId, Utente organizzatore) {
        Evento cercato =  getEventoById(eventoId);

        boolean isAdmin = organizzatore.getRuoli().stream()
                .anyMatch(ruolo -> ruolo.getNome().equals("ADMIN"));

        if (!isAdmin && !cercato.getOrganizzatore().getUtenteId().equals(organizzatore.getUtenteId())) {
            throw new AuthDeniedException("Non sei autorizzato a modificare l'evento");
        }
        eventoRepository.delete(cercato);
    }
    public Evento updateEvento(Evento evento) {

        if (!eventoRepository.existsById(evento.getEventoId())) {
            throw new NotFoundException("Evento con ID " + evento.getEventoId() + " non trovato");
        }
        return eventoRepository.save(evento);
    }
    public List<Evento> findEventiCarosello(int limit) {
        List<Evento> allEventi = eventoRepository.findAll();
        Collections.shuffle(allEventi);
        return allEventi.stream().limit(limit).collect(Collectors.toList());
    }
    public List<Evento> findEventiCaroselloSecondo(int limit) {
        List<Evento> allEventi = eventoRepository.findAll();
        Collections.shuffle(allEventi);
        return allEventi.stream().limit(limit).collect(Collectors.toList());
    }

    @Transactional
    public void deleteEventiPassati() {
        LocalDate today = LocalDate.now();
        eventoRepository.deleteEventiPassati(today);
    }


}



