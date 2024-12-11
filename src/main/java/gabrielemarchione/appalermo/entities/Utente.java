    package gabrielemarchione.appalermo.entities;


    import com.fasterxml.jackson.annotation.JsonIgnore;
    import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
    import com.fasterxml.jackson.annotation.JsonInclude;
    import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonIgnoreProperties({"accountNonLocked", "accountNonExpired", "credentialsNonExpired", "enabled", "authorities"})
    public class Utente implements UserDetails {
        @Id
        @GeneratedValue
        @Setter(AccessLevel.NONE)
        @Column(name = "utente_id")
        private UUID utenteId;
        @Column(nullable = false)
        @JsonIgnore
        private String username;
        @Column(nullable = false)
        private String email;
        @Column(nullable = false)
        @JsonIgnore
        private String password;
        @Column (nullable = false)
        private String  nome;
        @Column(nullable = false)
        private String cognome;



        @Column (name = "avatar_url", nullable = false)
        private String avatarUrl;


        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(name = "utenti_ruoli", joinColumns = @JoinColumn(name = "utente_id"), inverseJoinColumns = @JoinColumn(name = "ruolo_id"))
        @Setter (AccessLevel.NONE)
        private List<RuoloUtente>ruoli;

        @OneToMany(mappedBy = "utente")
        @JsonIgnore
        @Setter(AccessLevel.NONE)
        private List<Prenotazione>prenotazioni;

        @OneToMany(mappedBy = "organizzatore")
        @JsonIgnore
        @Setter(AccessLevel.NONE)
        private List<Evento>eventi;

        @OneToMany(mappedBy = "utente")
        @JsonIgnore
        @Setter (AccessLevel.NONE)
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
            this.ruoli.add(ruoloUtente);
        }

        @Override
        public String toString() {
            return "Utente{" +
                    "utenteId=" + utenteId +
                    ", username='" + username + '\'' +
                    ", nome='" + nome + '\'' +
                    ", cognome='" + cognome + '\'' +
                    ", email='" + email + '\'' +
                    ", ruoli=" + ruoli +
                    '}';
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty("username")
        public String getVisibleUsername() {
            return username;
        }
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return ruoli.stream().map(ruoloUtente -> new SimpleGrantedAuthority(ruoloUtente.getNome())).toList();
        }

    }