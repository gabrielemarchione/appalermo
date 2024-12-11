package gabrielemarchione.appalermo.services;


import com.cloudinary.Cloudinary;

import com.cloudinary.utils.ObjectUtils;
import gabrielemarchione.appalermo.dto.CambioPasswordDTO;
import gabrielemarchione.appalermo.dto.RuoloUtenteDTO;
import gabrielemarchione.appalermo.dto.UtenteDTO;
import gabrielemarchione.appalermo.dto.UtenteLoggatoDTO;
import gabrielemarchione.appalermo.entities.RuoloUtente;
import gabrielemarchione.appalermo.entities.Utente;
import gabrielemarchione.appalermo.exceptions.BadRequestException;
import gabrielemarchione.appalermo.exceptions.NotFoundException;
import gabrielemarchione.appalermo.repositories.UtenteRepository;
import gabrielemarchione.appalermo.tools.MailgunSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class UtenteService {
    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private PasswordEncoder bcrypt;
    @Autowired
    private Cloudinary cloudinaryUploader;
    @Autowired
    private MailgunSender mailgunSender;
    @Autowired RuoloUtenteService ruoloUtenteService;

    public Utente findUtenteById(UUID utenteId) {
        return utenteRepository.findById(utenteId).orElseThrow(() -> new NotFoundException("Utente con id " + utenteId + " non trovato"));
    }

    public Page<Utente> getAllUtenti(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return utenteRepository.findAll(pageable);
    }
    public Utente salvaUtente(UtenteDTO body) {
        if (utenteRepository.existsByEmail(body.email()))
            throw new BadRequestException("Email già in uso");
        if (utenteRepository.existsByUsername(body.username()))
            throw new BadRequestException("Username già in uso");
        Utente utente = new Utente(body.username(), body.email(), bcrypt.encode(body.password()), body.nome(), body.cognome());
        RuoloUtente ruoloUser = ruoloUtenteService.findRuoloUtenteByNome("USER");
        utente.addRuolo(ruoloUser);
        mailgunSender.sendRegistrationEmail(utente);
        return utenteRepository.save(utente);
    }
    public Utente cercaUtentePerUsername(String username) {
        return utenteRepository.findUtenteByUsername(username).orElseThrow(() -> new NotFoundException("Utente con " +
                "username " + username + " non trovato"));
    }

    public Utente creaAdmin(Utente utente) {
        return utenteRepository.save(utente);
    }

    public void deleteUtente(UUID utenteId) {
        Utente utente = findUtenteById(utenteId);
        utenteRepository.delete(utente);
    }

    public Utente modifiyUtenteAndUpdate(UUID utenteId, UtenteDTO body) {
        Utente searched = findUtenteById(utenteId);
        if (!searched.getCognome().equals(body.cognome()) || !searched.getNome().equals(body.nome())) {
            if (searched.getAvatarUrl().equals("https://ui-avatars.com/api/?name=" + searched.getNome() + "+" + searched.getCognome()))
                searched.setAvatarUrl("https://ui-avatars.com/api/?name=" + body.nome() + "+" + body.cognome());
        }
        if (!body.username().equals(searched.getUsername())) {
            if (utenteRepository.existsByUsername(body.username()))
                throw new BadRequestException("Username already in use");
            searched.setUsername(body.username());
        }
        if (!body.email().equals(searched.getEmail())) {
            if (utenteRepository.existsByEmail(body.email())) throw new BadRequestException("Email already in use");
            searched.setEmail(body.email());
        }
        searched.setPassword(bcrypt.encode(body.password()));
        searched.setNome(body.nome());
        searched.setCognome(body.cognome());
        return utenteRepository.save(searched);
    }


    public Utente modifiyUtenteLoggatoAndUpdate(UUID utenteId, UtenteLoggatoDTO body) {
        Utente searched = findUtenteById(utenteId);
        if (!searched.getCognome().equals(body.cognome()) || !searched.getNome().equals(body.nome())) {
            if (searched.getAvatarUrl().equals("https://ui-avatars.com/api/?name=" + searched.getNome() + "+" + searched.getCognome()))
                searched.setAvatarUrl("https://ui-avatars.com/api/?name=" + body.nome() + "+" + body.cognome());
        }
        if (!body.username().equals(searched.getUsername())) {
            if (utenteRepository.existsByUsername(body.username()))
                throw new BadRequestException("Username already in use");
            searched.setUsername(body.username());
        }
        if (!body.email().equals(searched.getEmail())) {
            if (utenteRepository.existsByEmail(body.email())) throw new BadRequestException("Email already in use");
            searched.setEmail(body.email());
        }
        searched.setNome(body.nome());
        searched.setCognome(body.cognome());
        return utenteRepository.save(searched);
    }

    public void modificaPassword(UUID utenteId, CambioPasswordDTO richiesta) {
        Utente utente = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new NotFoundException("Utente non trovato"));


        if (!bcrypt.matches(richiesta.passwordAttuale(), utente.getPassword())) {
            throw new BadRequestException("La password attuale non è corretta.");
        }

        if (!richiesta.nuovaPassword().equals(richiesta.confermaNuovaPassword())) {
            throw new BadRequestException("La nuova password e la conferma non coincidono.");
        }

        utente.setPassword(bcrypt.encode(richiesta.nuovaPassword()));
        utenteRepository.save(utente);
    }




    public Utente aggiungiRuolo(RuoloUtenteDTO body, UUID utenteId) {
        Utente cercato = findUtenteById(utenteId);
        RuoloUtente ruoloDaAggiungere = ruoloUtenteService.findRuoloUtenteByNome(body.nome());
        if (cercato.getRuoli().stream().anyMatch(ruoloUtente -> ruoloUtente.getRuoloUtenteId().equals(ruoloDaAggiungere.getRuoloUtenteId())))
            throw new BadRequestException("L'utente ha già il ruolo scelto");
        cercato.addRuolo(ruoloDaAggiungere);
        return utenteRepository.save(cercato);
    }

    public String uploadFotoProfilo(MultipartFile file, UUID utenteId) {
        try {
            String url = (String) cloudinaryUploader.uploader()
                    .upload(file.getBytes(), ObjectUtils.emptyMap())
                    .get("url");
            Utente trovato = this.findUtenteById(utenteId);
            trovato.setAvatarUrl(url);
            utenteRepository.save(trovato);
            return url;
        } catch (IOException e) {
            throw new BadRequestException("Errore durante l'upload dell'immagine!");
        }
    }

    public Utente rimuoviRuolo(RuoloUtenteDTO body, UUID utenteId) {
        Utente cercato = findUtenteById(utenteId);
        RuoloUtente ruoloDaRimuovere = ruoloUtenteService.findRuoloUtenteByNome(body.nome());
        boolean rimosso =
                cercato.getRuoli().removeIf(ruoloUtente -> ruoloUtente.getRuoloUtenteId().equals(ruoloDaRimuovere.getRuoloUtenteId()));
        if (rimosso) return utenteRepository.save(cercato);
        throw new BadRequestException("L'utente non possiede il ruolo fornito");
    }
}


