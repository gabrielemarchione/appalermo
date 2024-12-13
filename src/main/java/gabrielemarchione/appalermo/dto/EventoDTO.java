package gabrielemarchione.appalermo.dto;

import gabrielemarchione.appalermo.entities.enums.CategoriaEvento;
import jakarta.validation.constraints.*;


public record EventoDTO(
        @NotEmpty(message = "Il titolo deve essere fornito")
        @Size(min = 5, max = 255, message = "Il titolo deve avere almeno 5 caratteri")
        String titolo,
        @NotEmpty(message = "La descrizione deve essere fornita")
        @Size(min = 10, max = 255, message = "La descrizione deve avere almeno 10 caratteri")
        String descrizione,
        @NotEmpty(message = "La data dell'evento è obbligatoria!")
        String data,
        @NotEmpty(message = "Il luogo deve essere fornito")
        @Size(min = 3, max = 255, message = "Il luogo deve avere più di 2 caratteri")
        String luogo,
        @Min(value = 0, message = "Il costo non può essere negativo")
        double costo,
        @Min(value = 1, message = "L'evento deve avere almeno 1 posto")
        int postiMassimi,
        @Min(value = 0, message = "I posti disponibili non possono essere negativi")
        int postiDisponibili,
        @NotNull(message = "La categoria dell'evento deve essere fornita")
        CategoriaEvento categoriaEvento,
        String immagine
        )
{
}
