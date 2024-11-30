package gabrielemarchione.appalermo.entities;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import java.util.UUID;

@NoArgsConstructor
@Data
@Entity
@Table (name = "feedbacks")

public class Feedback {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    @Column (name = "feedback_id")
    private UUID feedbackId;
    @Column
    private Integer valutazione;
    @Column
    private String commento;
    @Column
    private LocalDate data;

    @ManyToOne
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    @ManyToOne
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;


    public Feedback(Integer valutazione, String commento, LocalDate data, Utente utente, Evento evento) {
        this.valutazione = valutazione;
        this.commento = commento;
        this.data = data;
        this.utente = utente;
        this.evento = evento;
    }
}

