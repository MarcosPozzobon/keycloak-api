package com.marcos.desenvolvimento.authorization_ms.service;

import com.marcos.desenvolvimento.authorization_ms.client.KeycloakClient;
import com.marcos.desenvolvimento.authorization_ms.dto.request.LoginRequest;
import com.marcos.desenvolvimento.authorization_ms.dto.response.TokenResponseDTO;
import com.marcos.desenvolvimento.authorization_ms.exception.GenericKeycloakException;
import com.marcos.desenvolvimento.authorization_ms.exception.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final KeycloakClient keycloakClient;

    @Value(value = "${spring.security.token.url}")
    private String keycloakTokenUrl;

    @Value(value = "${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    @Value(value = "${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String clientSecret;

    public TokenResponseDTO obterToken(final LoginRequest loginRequest) {
        var result =
                Optional.ofNullable(
                        keycloakClient.obterTokenKeycloakPorLoginESenha(
                                loginRequest.login(),
                                loginRequest.senha())
                ).orElse(null);

        if(result.contains("invalid_grant") || result.contains("Credenciais inválidas!")){
            throw new GenericKeycloakException("O usuário não existe ou as credenciais não conferem.");
        }

        JSONObject jsonResponse = new JSONObject(result);

        if(jsonResponse.getString("access_token") == null && jsonResponse.getString("access_token").isEmpty()){
            throw new InternalServerErrorException("O identity provider retornou null por algum motivo. Entre em contato com um admin.");
        }

        if(jsonResponse.getString("refresh_token") == null && jsonResponse.getString("refresh_token").isEmpty()){
            throw new InternalServerErrorException("Erro na resposta do identity provider. Entre em contato com um admin.");
        }

        return new TokenResponseDTO(jsonResponse.getString("access_token"), jsonResponse.getString("refresh_token"), jsonResponse.getInt("expires_in"));
    }

}


