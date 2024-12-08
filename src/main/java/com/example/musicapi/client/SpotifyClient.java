package com.example.musicapi.client;

import com.example.musicapi.model.Track;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SpotifyClient {
    private final RestTemplate restTemplate;

    @Value("${spotify.api.base-url}")
    private String baseUrl;

    @Value("${spotify.api.client-id}")
    private String clientId;

    @Value("${spotify.api.client-secret}")
    private String clientSecret;

    private String getAccessToken() {
        String tokenUrl = "https://accounts.spotify.com/api/token";
        Map<String, String> body = Map.of("grant_type", "client_credentials");
        Map<String, String> headers = Map.of(
                "Authorization", "Basic " + Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes())
        );

        Map<String, Object> response = restTemplate.postForObject(tokenUrl, body, Map.class, headers);
        return (String) response.get("access_token");
    }

    public Map<String, Object> fetchTrackMetadata(String isrc) {
        String url = String.format("%s/search?q=isrc:%s&type=track", baseUrl, isrc);
        return restTemplate.getForObject(url, Map.class);
    }

    public Map<String, Object> fetchAlbumDetails(String albumId) {
        String url = String.format("%s/albums/%s", baseUrl, albumId);
        return restTemplate.getForObject(url, Map.class);
    }
}
