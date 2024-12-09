package gabrielemarchione.appalermo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UtenteDTO(@NotEmpty(message = "Lo username deve essere fornito")
                        @Size(min = 4, max = 22, message = "Lo username deve essere tra 4 caratteri e 22 caratteri")
                        String username,
                        @NotEmpty(message = "La password deve essere fornita")
                        @Pattern(regexp = "^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#!$%^&+=]).*$",
                                message = "La password deve contenere almeno 8 caratteri, almeno un numero , almeno " +
                                        "un carattere minuscolo " +
                                        " almeno un carattere maiuscolo e un carattere speciale @#!$%^&+=")
                        String password,
                        @NotEmpty(message = "L'email deve essere fornita")
                        @Email(message = "Email non valida")
                        String email,
                        @NotEmpty(message = "Il nome deve essere fornito")
                        @Size(min = 2, max = 40, message = "Il nome deve contenere almeno due caratteri e non può " +
                                "superare 40 caratteri")
                        String nome,
                        @NotEmpty(message = "Il cognome deve essere fornito")
                        @Size(min = 2, max = 40, message = "Il cognome deve contenere almeno due caratteri e non può " +
                                "superare 40 caratteri")
                        String cognome) {
}
