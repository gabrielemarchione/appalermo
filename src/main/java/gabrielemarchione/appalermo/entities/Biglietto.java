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
@Table (name = "biglietti")

public class Biglietto {


    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    @Column(name = "biglietto_id")
    private UUID bigliettoId;
    @Column (name = "tipo")
    private String tipo;
    @Column (name = "prezzo")
    private double prezzo;
    @Column (name = "quantita_disponibile")
    private int quantitaDisponibile;

    @ManyToOne
    @JoinColumn (name = "evento_id", nullable = false)
    private Evento evento;

    public Biglietto(String tipo, double prezzo, int quantitaDisponibile, Evento evento) {
        this.tipo = tipo;
        this.prezzo = prezzo;
        this.quantitaDisponibile = quantitaDisponibile;
        this.evento = evento;
    }
}
