package gabrielemarchione.appalermo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PrenotazioneDTO(
        @NotNull( message = "evento id deve essere fornito")
        UUID eventoId,
        @NotNull (message = "Il numero di posti prenotati deve essere fornito")
        @Min( value = 1, message = "Devi prenotare almeno 1 posto")
        @Max(value = 8, message = "Puoi prenotare massimo 8 posti")
        int postiPrenotati)
{
}
