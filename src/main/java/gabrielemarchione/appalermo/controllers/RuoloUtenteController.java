package gabrielemarchione.appalermo.controllers;


import gabrielemarchione.appalermo.dto.RuoloUtenteDTO;
import gabrielemarchione.appalermo.entities.RuoloUtente;
import gabrielemarchione.appalermo.exceptions.BadRequestException;
import gabrielemarchione.appalermo.repositories.RuoloUtenteRepository;
import gabrielemarchione.appalermo.services.RuoloUtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ruoloUtenti")
public class RuoloUtenteController {
    @Autowired
    private RuoloUtenteService ruoloUtenteService;
    @Autowired
    private RuoloUtenteRepository ruoloUtenteRepository;

    @GetMapping
    public List<RuoloUtente> getAll() {
        return ruoloUtenteService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public RuoloUtente createRuoloUtente(@RequestBody @Validated RuoloUtenteDTO body, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message =
                    bindingResult.getAllErrors().stream().map(s -> s.getDefaultMessage()).collect(Collectors.joining(
                            ", "));
            throw new BadRequestException(message);
        }
        return ruoloUtenteService.createRuoloUtente(body);
    }

}