package com.marcos.desenvolvimento.authorization_ms.service;

import com.marcos.desenvolvimento.authorization_ms.client.KeycloakClient;
import com.marcos.desenvolvimento.authorization_ms.dto.request.LoginRequest;
import com.marcos.desenvolvimento.authorization_ms.dto.response.TokenResponseDTO;
import com.marcos.desenvolvimento.authorization_ms.exception.GenericKeycloakException;
import com.marcos.desenvolvimento.authorization_ms.exception.InternalServerErrorException;
import com.marcos.desenvolvimento.authorization_ms.exception.InvalidLoginRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginService {

    private final BeanUtils beanUtils;
    private final KeycloakClient keycloakClient;
    private final RedisService redisService;
    private final TokenService tokenService;

    @Value(value = "${spring.security.token.url}")
    private String keycloakTokenUrl;

    @Value(value = "${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    @Value(value = "${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String clientSecret;


    public TokenResponseDTO setUserAuthenticationContext(final LoginRequest loginRequest){
        if(!isLoginRequestValid(loginRequest)){
            throw new InvalidLoginRequestException("Invalid request!");
        }

        TokenResponseDTO authenticatedUserToken = getToken(loginRequest);
        var roles = getUserRolesByToken(authenticatedUserToken.token());

        redisService.setAuthenticationCache(authenticatedUserToken.token(), beanUtils.toJson(roles));
        return authenticatedUserToken;
    }

    private boolean isLoginRequestValid(LoginRequest loginRequest){

        if(loginRequest.username() == null || loginRequest.username().isEmpty()){
            return false;
        }
        if(loginRequest.password() == null || loginRequest.password().isEmpty()){
            return false;
        }
        return true;
    }

    public TokenResponseDTO getToken(final LoginRequest loginRequest) {
        var result =
                Optional.ofNullable(
                        keycloakClient.getKeycloakTokenByLoginAndPassword(
                                loginRequest.username(),
                                loginRequest.password())
                ).orElse(null);

        if(result.contains("invalid_grant") || result.contains("Invalid user credentials")){
            throw new GenericKeycloakException("The user does not exists or the credentials are not ok.");
        }

        JSONObject jsonResponse = new JSONObject(result);

        if(jsonResponse.getString("access_token") == null && jsonResponse.getString("access_token").isEmpty()){
            throw new InternalServerErrorException("Null api response from Keycloak. Contact an administrator.");
        }

        if(jsonResponse.getString("refresh_token") == null && jsonResponse.getString("refresh_token").isEmpty()){
            throw new InternalServerErrorException("Error in api response from Keycloak. Contact an administrator.");
        }

        return new TokenResponseDTO(jsonResponse.getString("access_token"), jsonResponse.getString("refresh_token"), jsonResponse.getInt("expires_in"));
    }

    public List<String> getUserRolesByToken(String token) {
        var claims = tokenService.getClaims(token);

        Map<String, Object> resourceAccess = (Map<String, Object>) claims.get("resource_access");
        if (resourceAccess == null) {
            return Collections.emptyList();
        }

        Map<String, Object> springClientAccess = (Map<String, Object>) resourceAccess.get("spring-client");
        if (springClientAccess == null) {
            return Collections.emptyList();
        }

        List<String> roles = (List<String>) springClientAccess.get("roles");
        return roles != null ? roles : Collections.emptyList();
    }

}


