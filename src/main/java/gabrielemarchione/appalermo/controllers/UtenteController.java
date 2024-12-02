package gabrielemarchione.appalermo.controllers;


import gabrielemarchione.appalermo.dto.RuoloUtenteDTO;
import gabrielemarchione.appalermo.dto.UtenteDTO;
import gabrielemarchione.appalermo.entities.Utente;
import gabrielemarchione.appalermo.exceptions.BadRequestException;
import gabrielemarchione.appalermo.services.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/utente")
public class UtenteController {
    @Autowired
    private UtenteService utenteService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Utente> getUtenti(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "username") String sortBy) {
        return utenteService.getAllUtenti(page, size, sortBy);
    }

    @DeleteMapping("/{utenteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUtente(@PathVariable UUID utenteId) {
        utenteService.deleteUtente(utenteId);
    }

    @GetMapping("/me")
    public Utente getLoggedUtente(@AuthenticationPrincipal Utente loggato) {
        return loggato;
    }

    @PutMapping("/me")
    public Utente modificaUtenteLoggato(@AuthenticationPrincipal Utente loggato,
                                        @RequestBody @Validated UtenteDTO body, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message =
                    bindingResult.getAllErrors().stream().map(s -> s.getDefaultMessage()).collect(Collectors.joining(
                            ", "));
            throw new BadRequestException(message);
        }
        return utenteService.modifiyUtenteAndUpdate(loggato.getUtenteId(), body);
    }

    @PatchMapping("/me/avatar")
    public String uploadFotoProfilo(@AuthenticationPrincipal Utente loggato, @RequestParam("foto") MultipartFile file) {
        return this.utenteService.uploadFotoProfilo(file, loggato.getUtenteId());
    }

    @PatchMapping("/{utenteId}/ruolo")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Utente aggiungiRuoloUtente(@PathVariable UUID utenteId, @RequestBody @Validated RuoloUtenteDTO body,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message =
                    bindingResult.getAllErrors().stream().map(s -> s.getDefaultMessage()).collect(Collectors.joining(
                            ", "));
            throw new BadRequestException(message);
        }
        return utenteService.aggiungiRuolo(body, utenteId);
    }

    @DeleteMapping("/{utenteId}/ruolo")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Utente rimuoviRuoloUtente(@PathVariable UUID utenteId, @RequestBody @Validated RuoloUtenteDTO body,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message =
                    bindingResult.getAllErrors().stream().map(s -> s.getDefaultMessage()).collect(Collectors.joining(
                            ", "));
            throw new BadRequestException(message);
        }
        return utenteService.rimuoviRuolo(body, utenteId);
    }
}
