package gabrielemarchione.appalermo.entities;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@Data
@Entity
@Table (name = "prenotazioni")

public class Prenotazione {
    @Id
    @GeneratedValue
    @Setter (AccessLevel.NONE)

    @Column (name = "prenotazione_id")
    private UUID prenotazioneId;

    @Column (name = "posti_prenotati")
    private int postiPrenotati;

    @Column (name = "prezzo_totale")
    private int prezzoTotale;


    @ManyToOne
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    @ManyToOne
    @JoinColumn (name = "evento_id", nullable = false)
    private Evento evento;

    public Prenotazione(int postiPrenotati, Utente utente, Evento evento) {
        this.postiPrenotati = postiPrenotati;
        this.utente = utente;
        this.evento = evento;
    }

    @Override
    public String toString() {
        return "Prenotazione{" +
                "prenotazioneId=" + prenotazioneId +
                ", postiPrenotati=" + postiPrenotati +
                ", utente=" + utente +
                ", evento=" + evento +
                '}';
    }
}
