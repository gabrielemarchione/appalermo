package gabrielemarchione.appalermo.runners;

import gabrielemarchione.appalermo.entities.RuoloUtente;
import gabrielemarchione.appalermo.entities.Utente;
import gabrielemarchione.appalermo.services.RuoloUtenteService;
import gabrielemarchione.appalermo.services.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class adminDbRunner implements CommandLineRunner {
    @Autowired
    private UtenteService utenteService;
    @Autowired
    private RuoloUtenteService ruoloUtenteService;
    @Autowired
    private PasswordEncoder bcrypt;
    @Autowired
    @Value("${ADMIN_NOME}")
    private String nomeAdmin;
    @Value("${ADMIN_COGNOME}")
    private String cognomeAdmin;
    @Value("${ADMIN_USERNAME}")
    private String usernameAdmin;
    @Value("${ADMIN_EMAIL}")
    private String emailAdmin;
    @Value("${ADMIN_PASSWORD}")
    private String passwordAdmin;


    private void aggiungiAdmin() {
        Utente admin = new Utente(usernameAdmin, emailAdmin, bcrypt.encode(passwordAdmin), nomeAdmin, cognomeAdmin);
        RuoloUtente ruoloAdmin = ruoloUtenteService.findRuoloUtenteByNome("ADMIN");
        if (ruoloAdmin == null) {
            throw new RuntimeException("Ruolo ADMIN non trovato nel database");
        }
        admin.addRuolo(ruoloAdmin);
        utenteService.creaAdmin(admin);
    }

    private void populateRuoli() {
        RuoloUtente admin = new RuoloUtente("ADMIN");
        RuoloUtente user = new RuoloUtente("USER");
        RuoloUtente organizzatore = new RuoloUtente("ORGANIZZATORE");
        ruoloUtenteService.saveRuoloUtente(admin);
        ruoloUtenteService.saveRuoloUtente(organizzatore);
        ruoloUtenteService.saveRuoloUtente(user);
    }
    @Override
    public void run(String... args) throws Exception {
        if (ruoloUtenteService.findAll().isEmpty()) populateRuoli();
        if (utenteService.getAllUtenti(0, 100000, "nome").isEmpty()) aggiungiAdmin();
    }
}
