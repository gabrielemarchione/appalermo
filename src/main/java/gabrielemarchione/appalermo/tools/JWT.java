package gabrielemarchione.appalermo.tools;


import gabrielemarchione.appalermo.entities.RuoloUtente;
import gabrielemarchione.appalermo.entities.Utente;
import gabrielemarchione.appalermo.exceptions.UnauthorizedException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JWT {
    @Value("${jwt.secret}")
    private String secret;

    public String generaToken(Utente utente) {
        // mi recupero i ruoli di quell'utente

        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 12)) //12h
                .subject(String.valueOf(utente.getUtenteId()))
                .claim("nome", utente.getNome())
                .claim("ruoli", utente.getRuoli().stream().map(RuoloUtente::getNome).collect(Collectors.toList()))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    public void verificaToken(String token) {
        try {
            Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parse(token);
        } catch (Exception e) {
            throw new UnauthorizedException("Token non valido");
        }
    }

    public String estraiUtenteIdDalToken(String token) {
        return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parseSignedClaims(token).getPayload().getSubject();
    }
}
