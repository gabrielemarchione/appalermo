package gabrielemarchione.appalermo.entities;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
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
    private Integer postiPrenotati;

    @OneToMany(mappedBy = "prenotazione")
    private List <PrenotazioneBiglietto> prenotazioneBiglietto;

    @ManyToOne
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    @ManyToOne
    @JoinColumn (name = "evento_id", nullable = false)
    private Evento evento;

    public Prenotazione(Integer postiPrenotati, Utente utente, Evento evento) {
        this.postiPrenotati = postiPrenotati;
        this.utente = utente;
        this.evento = evento;
    }
}
