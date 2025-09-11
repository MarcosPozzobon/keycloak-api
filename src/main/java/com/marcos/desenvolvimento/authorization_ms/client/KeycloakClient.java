package com.marcos.desenvolvimento.authorization_ms.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class KeycloakClient {

    @Value(value = "${spring.security.token.url}")
    private String keycloakTokenUrl;

    @Value(value = "${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    @Value(value = "${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String clientSecret;

    public String obterTokenKeycloakPorLoginESenha(String login, String senha) {
        try {
            String body = "grant_type=password"
                    + "&client_id=" + clientId
                    + "&client_secret=" + clientSecret
                    + "&username=" + login
                    + "&password=" + senha;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(keycloakTokenUrl))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao obter token do Keycloak", e);
        }
    }
}
