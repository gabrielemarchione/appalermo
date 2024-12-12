package gabrielemarchione.appalermo.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private int postiMassimi;
    @Column
    private int postiDisponibili;
    @Enumerated(EnumType.STRING)
    private CategoriaEvento categoriaEvento;


    @ManyToOne
    @JoinColumn(name = "organizzatore_id", nullable = false)
    private Utente organizzatore;


    @OneToMany(mappedBy = "evento" )
    @JsonIgnore
    private List<Prenotazione> prenotazioni;

    @OneToMany (mappedBy = "evento")
    private List<Feedback> feedbacks;

    public Evento( String titolo,String descrizione, LocalDate data, String luogo, double costo, int postiMassimi, int postiDisponibili, CategoriaEvento categoriaEvento, Utente organizzatore) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.data = data;
        this.luogo = luogo;
        this.costo = costo;
        this.postiMassimi = postiMassimi;
        this.postiDisponibili = postiDisponibili;
        this.categoriaEvento = categoriaEvento;
        this.organizzatore = organizzatore;
    }

    @Override
    public String toString() {
        return "Evento{" +
                "eventoId=" + eventoId +
                ", titolo='" + titolo + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", data=" + data +
                ", luogo='" + luogo + '\'' +
                ", costo=" + costo +
                ", postiMassimi=" + postiMassimi +
                ", categoriaEvento=" + categoriaEvento +
                ", organizzatore=" + organizzatore +
                '}';
    }
}
