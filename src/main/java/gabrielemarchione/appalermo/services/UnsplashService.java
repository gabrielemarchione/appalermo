package gabrielemarchione.appalermo.services;


import gabrielemarchione.appalermo.entities.enums.CategoriaEvento;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class UnsplashService {

    @Value("${unsplash.api.url}")
    private String apiUrl;

    @Value("${unsplash.api.key}")
    private String apiKey;

    public String getImageByCategory(CategoriaEvento categoria) {
        String keyword = categoria.getKeyword(); // Ottiene la keyword associata all'enum
        String url = "https://api.unsplash.com/photos/random?query=" + keyword + "&client_id=" + apiKey;

        try {
            RestTemplate restTemplate = new RestTemplate();
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.containsKey("urls")) {
                Map<String, String> urls = (Map<String, String>) response.get("urls");
                return urls.getOrDefault("regular", "https://via.placeholder.com/800x400");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "https://via.placeholder.com/800x400";
    }
}

