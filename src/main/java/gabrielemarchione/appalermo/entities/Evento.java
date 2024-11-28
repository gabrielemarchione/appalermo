package gabrielemarchione.appalermo.entities;


import gabrielemarchione.appalermo.entities.enums.CategoriaEvento;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "eventi")

public class Evento {


    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    @Column(name = "eventi_Id")
    private UUID eventoId;
    @Column
    private String titolo;
    @Column
    private String descrizione;
    @Column
    private LocalDate data;
    @Column
    private String luogo;
    @Column
    private double costo;
    @Column
    private int posti_massimi;
    @Enumerated(EnumType.STRING)
    private CategoriaEvento categoriaEvento;

    @ManyToOne
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    @OneToMany(mappedBy = "evento" )
    private List<Prenotazione> prenotazioni;

    @OneToMany(mappedBy = "evento")
    private List <Biglietto> biglietti;

    @OneToMany (mappedBy = "evento")
    private List<Feedback> feedbacks;

    public Evento(String descrizione, String titolo, LocalDate data, String luogo, double costo, int posti_massimi, CategoriaEvento categoriaEvento, Utente utente) {
        this.descrizione = descrizione;
        this.titolo = titolo;
        this.data = data;
        this.luogo = luogo;
        this.costo = costo;
        this.posti_massimi = posti_massimi;
        this.categoriaEvento = categoriaEvento;
        this.utente = utente;
    }


}
