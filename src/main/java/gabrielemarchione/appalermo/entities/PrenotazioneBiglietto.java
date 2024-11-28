package gabrielemarchione.appalermo.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "prenotazioni_biglietti")
public class PrenotazioneBiglietto implements Serializable {

    @Id
    @GeneratedValue()
    @Column
    private UUID prenotazioneBigliettoId;

    @ManyToOne
    @JoinColumn(name = "prenotazione_id", nullable = false)
    private Prenotazione prenotazione;

    @ManyToOne
    @JoinColumn(name = "biglietto_id", nullable = false)
    private Biglietto biglietto;

    @Column(nullable = false)
    private int quantita;

    public PrenotazioneBiglietto(Prenotazione prenotazione, Biglietto biglietto, int quantita) {
        this.prenotazione = prenotazione;
        this.biglietto = biglietto;
        this.quantita = quantita;
    }
}
