package gabrielemarchione.appalermo.services;


import gabrielemarchione.appalermo.dto.UtenteLoginDTO;
import gabrielemarchione.appalermo.entities.Utente;
import gabrielemarchione.appalermo.exceptions.NotFoundException;
import gabrielemarchione.appalermo.exceptions.UnauthorizedException;
import gabrielemarchione.appalermo.tools.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private JWT jwt;
    @Autowired
    private PasswordEncoder bcrypt;
    @Autowired
    private UtenteService utenteService;

    public String generaToken(UtenteLoginDTO body) {
        try {
            Utente loggato = utenteService.cercaUtentePerUsername(body.username());
            if (bcrypt.matches(body.password(), loggato.getPassword()))
                return jwt.generaToken(loggato);
            throw new UnauthorizedException("Credenziali non valide");
        } catch (NotFoundException e) {
            throw new UnauthorizedException("Credenziali non valide");
        }

    }
}
