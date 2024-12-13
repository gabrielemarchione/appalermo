package gabrielemarchione.appalermo.dto;

import jakarta.validation.constraints.NotBlank;


public record CambioPasswordDTO(
        @NotBlank(message = "La password attuale è obbligatoria.")
        String passwordAttuale,

        @NotBlank(message = "La nuova password è obbligatoria.")
        String nuovaPassword,

        @NotBlank(message = "La conferma della nuova password è obbligatoria.")
        String confermaNuovaPassword
) {
}
