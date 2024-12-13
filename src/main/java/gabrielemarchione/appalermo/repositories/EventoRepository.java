package gabrielemarchione.appalermo.repositories;

import gabrielemarchione.appalermo.entities.Evento;
import gabrielemarchione.appalermo.entities.Utente;
import gabrielemarchione.appalermo.entities.enums.CategoriaEvento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface EventoRepository extends JpaRepository<Evento, UUID> {
    List<Evento> findByOrganizzatore(Utente organizzatore);

    @Query("SELECT e FROM Evento e WHERE " +
            "(:titolo IS NULL OR e.titolo LIKE %:titolo%) AND " +
            "(:data IS NULL OR e.data = :data) AND " +
            "(:categoriaEvento IS NULL OR e.categoriaEvento = :categoriaEvento) AND " +
            "(:costo IS NULL OR e.costo <= :costo)")
    Page<Evento> findFilteredEvents(
            @Param("titolo") String titolo,
            @Param("data") LocalDate data,
            @Param("categoriaEvento") CategoriaEvento categoriaEvento,
            @Param("costo") Double costo,
            Pageable pageable
    );


}
