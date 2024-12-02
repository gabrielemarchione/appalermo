package gabrielemarchione.appalermo.controllers;


import gabrielemarchione.appalermo.dto.PrenotazioneDTO;
import gabrielemarchione.appalermo.entities.Prenotazione;
import gabrielemarchione.appalermo.entities.Utente;
import gabrielemarchione.appalermo.exceptions.BadRequestException;
import gabrielemarchione.appalermo.services.PrenotazioneService;
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
@RequestMapping("/prenotazioni")
public class PrenotazioneController {
    @Autowired
    private PrenotazioneService prenotazioneService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Prenotazione> getAllPrenotazioni(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(defaultValue = "event.date") String sortBy) {
        return prenotazioneService.findAllPrenotazioni(page, size, sortBy);
    }

    @GetMapping("/{prenotazioneId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Prenotazione getSingolaPrenotazione(@PathVariable UUID prenotazioneId) {
        return prenotazioneService.findPrenotazioneById(prenotazioneId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Prenotazione savePrenotazione(@AuthenticationPrincipal Utente current, @RequestBody @Validated PrenotazioneDTO body,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message =
                    bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));
            throw new BadRequestException(message);
        }
        return prenotazioneService.savePrenotazione(body, current);

    }

    @GetMapping("/lemieprenotazioni")
    public List<Prenotazione> getAllBookingsByCurrentUser(@AuthenticationPrincipal Utente current) {
        return prenotazioneService.findPrenotazioneByUtente(current);
    }

    @DeleteMapping("/mybookings/{prenotazioneId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePrenotazione(@AuthenticationPrincipal Utente current, @PathVariable UUID prenotazioneId) {
        prenotazioneService.cancellaPrenotazioneById(prenotazioneId, current);
    }
}
