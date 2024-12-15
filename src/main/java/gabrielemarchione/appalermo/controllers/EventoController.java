package gabrielemarchione.appalermo.controllers;


import gabrielemarchione.appalermo.dto.EventoDTO;
import gabrielemarchione.appalermo.entities.Evento;
import gabrielemarchione.appalermo.entities.Utente;
import gabrielemarchione.appalermo.entities.enums.CategoriaEvento;
import gabrielemarchione.appalermo.exceptions.BadRequestException;
import gabrielemarchione.appalermo.services.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/evento")
public class EventoController {
    @Autowired
    private EventoService eventoService;

    @GetMapping
    public Page<Evento> getAllEvento(
            @RequestParam(required = false) String titolo,
            @RequestParam(required = false, value = "data") LocalDate data,
            @RequestParam(required = false) CategoriaEvento categoriaEvento,
            @RequestParam(required = false) Double costo,
            @RequestParam(value = "organizzatore", required = false) String organizzatore,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size,
            @RequestParam(defaultValue = "data") String sortBy) {
        eventoService.deleteEventiPassati();

        return eventoService.findAllEvento(titolo, data, categoriaEvento, costo, organizzatore, page, size, sortBy);
    }


    @PostMapping("/crea")
    @PreAuthorize("hasAnyAuthority('ORGANIZZATORE','ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Evento saveEvento(@AuthenticationPrincipal Utente organizzatore, @RequestBody @Validated EventoDTO body,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message =
                    bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));
            throw new BadRequestException(message);
        }
        return eventoService.saveEvento(body, organizzatore);
    }

    @GetMapping("/imieieventi")
    @PreAuthorize("hasAnyAuthority('ORGANIZZATORE','ADMIN')")
    public List<Evento> getAllOrganizerEvents(@AuthenticationPrincipal Utente organizzatore) {
        return eventoService.findAllEventoByOrganizzatore(organizzatore);
    }

    @PatchMapping("/imieieventi/{eventoId}")
    @PreAuthorize("hasAnyAuthority('ORGANIZZATORE','ADMIN')")
    public Evento modifyEvent(@AuthenticationPrincipal Utente organizzatore, @RequestBody @Validated EventoDTO body,
                             BindingResult bindingResult, @PathVariable UUID eventoId) {
        if (bindingResult.hasErrors()) {
            String message =
                    bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));
            throw new BadRequestException(message);
        }
        return eventoService.findEventoByIdAndUpdate(body, eventoId, organizzatore);
    }

    @DeleteMapping("/imieieventi/{eventoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ORGANIZZATORE', 'ADMIN')")
    public void deleteEvent(@AuthenticationPrincipal Utente organizzatore, @PathVariable UUID eventoId) {
        eventoService.deleteEvento(eventoId, organizzatore);
    }

    @GetMapping("/carosello")
    public List<Evento> getCarouselEvents() {
        return eventoService.findEventiCarosello(3);
    public List<Evento> getCarouselEvents(@RequestParam(defaultValue = "5") int limit) {
        return eventoService.findEventiCarosello(limit);
    }

    @GetMapping("/carosello-secondo")
    public List<Evento> getSecondCarouselEvents(@RequestParam(defaultValue = "10") int limit) {
        return eventoService.findEventiCaroselloSecondo(limit);
    }


}
