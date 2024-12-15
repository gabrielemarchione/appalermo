package gabrielemarchione.appalermo.controllers;


import gabrielemarchione.appalermo.entities.enums.CategoriaEvento;
import gabrielemarchione.appalermo.services.UnsplashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UnsplashController {

    @Autowired
    private UnsplashService unsplashService;

    @GetMapping("/api/random-event-image")
    public String getImageByCategory(@RequestParam String categoriaEvento) {

        try {
            // Usa il metodo fromString per ottenere la costante dell'enum
            CategoriaEvento categoria = CategoriaEvento.fromString(categoriaEvento);

            // Ottiene la keyword associata all'enum
            String keyword = categoria.getKeyword();

            // Costruisce l'URL per Unsplash
            String url = "https://api.unsplash.com/photos/random?query=" + keyword + "&w=1920&h=1080&client_id=YOUR_API_KEY";

            // Passa l'enum
            return unsplashService.getImageByCategory(categoria);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return "https://via.placeholder.com/800x400"; // in caso di errore
        }
    }}







