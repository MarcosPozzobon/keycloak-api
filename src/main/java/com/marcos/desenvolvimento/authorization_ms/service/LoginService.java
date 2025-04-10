package com.marcos.desenvolvimento.authorization_ms.service;

import com.marcos.desenvolvimento.authorization_ms.dto.request.LoginRequest;
import com.marcos.desenvolvimento.authorization_ms.dto.response.TokenResponseDTO;
import com.marcos.desenvolvimento.authorization_ms.exception.InternalServerErrorException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class LoginService {

    @Value(value = "${spring.security.token.url}")
    private String keycloakTokenUrl;

    @Value(value = "${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    @Value(value = "${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;

    public LoginService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public TokenResponseDTO authenticate(final LoginRequest loginRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("username", loginRequest.username());
        params.add("password", loginRequest.password());

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                keycloakTokenUrl,
                HttpMethod.POST,
                entity,
                Map.class
        );

        if(response.getStatusCode().is5xxServerError() || response.getBody() == null){
            throw new InternalServerErrorException("Failed to obtain the accessToken. Call an administrator for more details.");
        }

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Map<String, Object> responseBody = response.getBody();

            TokenResponseDTO tokenResponseDTO = new TokenResponseDTO(
                    (String) responseBody.get("access_token"),
                    (String) responseBody.get("refresh_token"),
                    ((Number) responseBody.get("expires_in")).intValue()
            );

            return tokenResponseDTO;
        }
        return null;
    }

}
