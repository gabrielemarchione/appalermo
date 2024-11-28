package gabrielemarchione.appalermo.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "ruoli_utente")
@JsonIgnoreProperties({"utenti"})
public class RuoloUtente {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    @Column(name = "ruolo_utente_id")
    private UUID ruoloUtenteId;
    @Column(nullable = false)
    private String nome;
    @ManyToMany(mappedBy = "ruoli")
    private List<Utente> utenti;

    public RuoloUtente(String nome) {
        this.nome = nome;
    }
}
