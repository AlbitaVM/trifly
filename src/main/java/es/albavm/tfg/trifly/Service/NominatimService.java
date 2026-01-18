package es.albavm.tfg.trifly.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class NominatimService {

    @Value("${nominatim.geocoding.url}")
    private String geocodingUrl;

    @Value("${nominatim.user.agent}")
    private String userAgent;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private long lastRequestTime = 0;

    public NominatimService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Espera el tiempo necesario para respetar el límite de 1 req/segundo de Nominatim
     */
    private void respectRateLimit() {
        long currentTime = System.currentTimeMillis();
        long timeSinceLastRequest = currentTime - lastRequestTime;
        
        // Nominatim requiere mínimo 1 segundo entre peticiones
        if (timeSinceLastRequest < 1000) {
            try {
                Thread.sleep(1000 - timeSinceLastRequest);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        lastRequestTime = System.currentTimeMillis();
    }

    /**
     * Geocodifica una dirección usando Nominatim (OpenStreetMap)
     * @param address Dirección completa (ej: "Torre Eiffel, París, Francia")
     * @return Map con "latitude" y "longitude", o null si no se encuentra
     */
    public Map<String, Double> geocodeAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            System.out.println("NominatimService: dirección vacía o nula");
            return null;
        }

        try {
            // Respetar el rate limit de Nominatim (1 req/segundo)
            respectRateLimit();

            // Construir la URL
            String url = UriComponentsBuilder
                .fromHttpUrl(geocodingUrl)
                .queryParam("q", address.trim())
                .queryParam("format", "json")
                .queryParam("limit", 1)
                .queryParam("addressdetails", 1)
                .build()
                .toUriString();

            // Configurar headers (Nominatim requiere User-Agent)
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", userAgent);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            System.out.println("NominatimService: Geocodificando -> " + address);

            // Hacer la petición
            ResponseEntity<String> response = restTemplate.exchange(
                url, 
                HttpMethod.GET, 
                entity, 
                String.class
            );

            // Parsear la respuesta JSON
            JsonNode root = objectMapper.readTree(response.getBody());

            if (root.isArray() && root.size() > 0) {
                JsonNode firstResult = root.get(0);
                
                String lat = firstResult.get("lat").asText();
                String lon = firstResult.get("lon").asText();

                double latitude = Double.parseDouble(lat);
                double longitude = Double.parseDouble(lon);

                Map<String, Double> coordinates = new HashMap<>();
                coordinates.put("latitude", latitude);
                coordinates.put("longitude", longitude);

                System.out.println("NominatimService: ✓ Geocodificado -> Lat: " + latitude + ", Lng: " + longitude);
                return coordinates;
            }

            System.out.println("NominatimService: ✗ No se encontraron resultados para: " + address);
            return null;

        } catch (Exception e) {
            System.err.println("NominatimService: ERROR geocodificando '" + address + "': " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Geocodifica con información adicional del lugar
     * @param address Dirección a geocodificar
     * @return Map con toda la información disponible
     */
    public Map<String, Object> geocodeWithDetails(String address) {
        if (address == null || address.trim().isEmpty()) {
            return null;
        }

        try {
            respectRateLimit();

            String url = UriComponentsBuilder
                .fromHttpUrl(geocodingUrl)
                .queryParam("q", address.trim())
                .queryParam("format", "json")
                .queryParam("limit", 1)
                .queryParam("addressdetails", 1)
                .build()
                .toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", userAgent);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                url, 
                HttpMethod.GET, 
                entity, 
                String.class
            );

            JsonNode root = objectMapper.readTree(response.getBody());

            if (root.isArray() && root.size() > 0) {
                JsonNode firstResult = root.get(0);
                
                Map<String, Object> details = new HashMap<>();
                
                // Coordenadas
                details.put("latitude", Double.parseDouble(firstResult.get("lat").asText()));
                details.put("longitude", Double.parseDouble(firstResult.get("lon").asText()));
                
                // Nombre del lugar
                if (firstResult.has("display_name")) {
                    details.put("displayName", firstResult.get("display_name").asText());
                }
                
                // Tipo de lugar
                if (firstResult.has("type")) {
                    details.put("type", firstResult.get("type").asText());
                }
                
                // Detalles de dirección
                if (firstResult.has("address")) {
                    JsonNode address_node = firstResult.get("address");
                    Map<String, String> addressMap = new HashMap<>();
                    
                    if (address_node.has("city")) {
                        addressMap.put("city", address_node.get("city").asText());
                    }
                    if (address_node.has("country")) {
                        addressMap.put("country", address_node.get("country").asText());
                    }
                    if (address_node.has("country_code")) {
                        addressMap.put("countryCode", address_node.get("country_code").asText());
                    }
                    if (address_node.has("postcode")) {
                        addressMap.put("postcode", address_node.get("postcode").asText());
                    }
                    
                    details.put("address", addressMap);
                }
                
                return details;
            }

            return null;

        } catch (Exception e) {
            System.err.println("NominatimService: Error geocodificando con detalles: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}