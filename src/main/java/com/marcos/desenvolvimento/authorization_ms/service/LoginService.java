package com.marcos.desenvolvimento.authorization_ms.service;

import com.marcos.desenvolvimento.authorization_ms.dto.request.LoginRequest;
import com.marcos.desenvolvimento.authorization_ms.dto.response.TokenResponseDTO;
import com.marcos.desenvolvimento.authorization_ms.exception.AuthenticationContextException;
import com.marcos.desenvolvimento.authorization_ms.exception.InternalServerErrorException;
import com.marcos.desenvolvimento.authorization_ms.exception.InvalidLoginRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final BeanUtils beanUtils;
    private final RestTemplate restTemplate;
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

        TokenResponseDTO authenticatedUserToken = authenticate(loginRequest);
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

    public List<String> getUserRolesByToken(String token) {
        Map<String, Object> claims = tokenService.getClaims(token);

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

    public boolean isAuthenticationContextValid(String authorizationHeader){
        if(!tokenService.isValidToken(authorizationHeader)){
            throw new AuthenticationContextException("The Authorization header must be present."); // sempre vai retornar 401
        }

        //TODO

        return false;
    }

}
