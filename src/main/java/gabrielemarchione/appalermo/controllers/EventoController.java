package gabrielemarchione.appalermo.controllers;


import gabrielemarchione.appalermo.dto.EventoDTO;
import gabrielemarchione.appalermo.entities.Evento;
import gabrielemarchione.appalermo.entities.Utente;
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

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/evento")
public class EventoController {
    @Autowired
    private EventoService eventoService;

    @GetMapping
    public Page<Evento> getAllEvento(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     @RequestParam(defaultValue = "data") String sortBy) {
        return eventoService.findAllEvento(page, size, sortBy);
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

    @PutMapping("/imieieventi/{eventoId}")
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


}
