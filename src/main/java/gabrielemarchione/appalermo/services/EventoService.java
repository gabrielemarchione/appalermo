package gabrielemarchione.appalermo.services;

import gabrielemarchione.appalermo.dto.EventoDTO;
import gabrielemarchione.appalermo.entities.Evento;
import gabrielemarchione.appalermo.entities.Utente;
import gabrielemarchione.appalermo.exceptions.AuthDeniedException;
import gabrielemarchione.appalermo.exceptions.BadRequestException;
import gabrielemarchione.appalermo.exceptions.NotFoundException;
import gabrielemarchione.appalermo.repositories.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

@Service
public class EventoService {
    @Autowired
    private EventoRepository eventoRepository;
    @Autowired
    private List<DateTimeFormatter> formatters;

    public Page<Evento> findAllEvento(int page, int size,String sortBy){
        Pageable pageable = PageRequest.of(page,size, Sort.by(sortBy));
        return eventoRepository.findAll(pageable);
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
    public Evento saveEvento (EventoDTO body, Utente organizzatore){
        LocalDate data = validateDate(body.data());
        return eventoRepository.save(new Evento(body.titolo(), body.descrizione(),data, body.luogo(), body.costo(),
                 body.postiMassimi(), body.postiDisponibili(), body.categoriaEvento(), organizzatore));
    }

    public List<Evento> findAllEventoByOrganizzatore(Utente organizzatore) {
        return eventoRepository.findByOrganizzatore(organizzatore);
    }

    public Evento findEventoByIdAndUpdate(EventoDTO body, UUID eventoId, Utente organizzatore) {
        Evento cercato = getEventoById(eventoId);
        if (!cercato.getOrganizzatore().getUtenteId().equals(organizzatore.getUtenteId()))
            throw new AuthDeniedException("Non sei autorizzato a modificare l'evento");
        cercato.setData(validateDate(body.data()));
        cercato.setDescrizione(body.descrizione());
        cercato.setTitolo(body.titolo());
        cercato.setLuogo(body.luogo());
        cercato.setPostiMassimi(body.postiMassimi());
        return eventoRepository.save(cercato);
    }
    public void deleteEvento(UUID eventoId, Utente organizzatore) {
        Evento cercato =  getEventoById(eventoId);
        if (!cercato.getOrganizzatore().getUtenteId().equals(organizzatore.getUtenteId()))
            throw new AuthDeniedException("Non sei autorizzato a cancellare l'evento");
        if (cercato.getPrenotazioni() != null)
            throw new BadRequestException("Ci sono già prenotazioni, non puoi cancellare l'evento");
        eventoRepository.delete(cercato);
    }
}
