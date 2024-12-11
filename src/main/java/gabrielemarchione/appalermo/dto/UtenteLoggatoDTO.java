package gabrielemarchione.appalermo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UtenteLoggatoDTO(
        String username,
        String email,
        String nome,
        String cognome,
        String password) {
}
