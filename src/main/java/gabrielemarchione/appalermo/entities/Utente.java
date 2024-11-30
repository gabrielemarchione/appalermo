    package gabrielemarchione.appalermo.entities;


    import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
    import jakarta.persistence.*;
    import lombok.AccessLevel;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import lombok.Setter;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;


    import java.util.ArrayList;
    import java.util.Collection;
    import java.util.List;
    import java.util.UUID;

    @Data
    @NoArgsConstructor
    @Entity
    @Table (name= "utenti")
    @JsonIgnoreProperties({"accountNonLocked", "accountNonExpired", "credentialsNonExpired", "enabled", "authorities", "password", "username"})
    public class Utente implements UserDetails {
        @Id
        @GeneratedValue
        @Setter(AccessLevel.NONE)
        @Column(name = "utente_id")
        private UUID utenteId;
        @Column(nullable = false)
        private String username, email, password, nome, cognome;
        @Column (name = "avatar_url", nullable = false)
        private String avatarUrl;

        @Column (name = "ruolo_id")
        private String ruolo;

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(name = "utenti_ruoli", joinColumns = @JoinColumn(name = "utente_id"), inverseJoinColumns = @JoinColumn(name = "ruolo_id"))
        @Setter (AccessLevel.NONE)
        private List<RuoloUtente>ruoli = new ArrayList<>();

        @OneToMany(mappedBy = "utente")
        private List<Prenotazione> prenotazioni;

        @OneToMany(mappedBy = "organizzatore")
        private List<Evento> eventi;

        @OneToMany(mappedBy = "utente")
        private List<Feedback>feedbacks;


    public Utente( String username, String email, String password, String nome, String cognome) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.nome = nome;
        this.cognome = cognome;
        this.avatarUrl = "https://ui-avatars.com/api/?name=" +
                nome + "+" + cognome;
        this.ruoli = new ArrayList<>();
    }



        public void addRuolo(RuoloUtente ruoloUtente) {
            if (!this.ruoli.contains(ruoloUtente)) {
                this.ruoli.add(ruoloUtente);
            }
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return ruoli.stream().map(ruoloUtente -> new SimpleGrantedAuthority(ruoloUtente.getNome())).toList();
        }

    }