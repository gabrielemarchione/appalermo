package gabrielemarchione.appalermo.controllers;


import gabrielemarchione.appalermo.dto.UtenteDTO;
import gabrielemarchione.appalermo.dto.UtenteLoginDTO;
import gabrielemarchione.appalermo.dto.UtenteTokenDTO;
import gabrielemarchione.appalermo.entities.Utente;
import gabrielemarchione.appalermo.exceptions.BadRequestException;
import gabrielemarchione.appalermo.services.AuthService;
import gabrielemarchione.appalermo.services.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UtenteService utenteService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public Utente registraUser(@RequestBody @Validated UtenteDTO body, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message =
                    bindingResult.getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.joining(
                            ", "));
            throw new BadRequestException(message);
        }
        return utenteService.salvaUtente(body);
    }

    @PostMapping("/login")
    public UtenteTokenDTO login(@RequestBody @Validated UtenteLoginDTO body, BindingResult bindingResult) throws BadRequestException{
        if (bindingResult.hasErrors()) {
            String message =
                    bindingResult.getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.joining(
                            ", "));
            throw new BadRequestException("password sbagliata");
        }
        return new UtenteTokenDTO(authService.generaToken(body));
    }
}
