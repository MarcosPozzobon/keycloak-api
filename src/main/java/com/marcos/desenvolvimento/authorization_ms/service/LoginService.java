package com.marcos.desenvolvimento.authorization_ms.service;

import com.marcos.desenvolvimento.authorization_ms.client.KeycloakClient;
import com.marcos.desenvolvimento.authorization_ms.dto.request.LoginRequest;
import com.marcos.desenvolvimento.authorization_ms.dto.response.TokenResponseDTO;
import com.marcos.desenvolvimento.authorization_ms.exception.AuthenticationContextException;
import com.marcos.desenvolvimento.authorization_ms.exception.InternalServerErrorException;
import com.marcos.desenvolvimento.authorization_ms.exception.InvalidLoginRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
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

        TokenResponseDTO authenticatedUserToken = authenticate(loginRequest);
        //var roles = getUserRolesByToken(authenticatedUserToken.token());

       // redisService.setAuthenticationCache(authenticatedUserToken.token(), beanUtils.toJson(roles));
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

        String result = keycloakClient.getKeycloakTokenByLoginAndPassword(loginRequest.username(), loginRequest.password());
        JSONObject jsonResponse = new JSONObject(result);

        if(jsonResponse.getString("acessToken") != null && !jsonResponse.getString("accessToken").isEmpty()){

        }




            return new TokenResponseDTO(null, null, null);
        }
        //return null;
    }

    /*
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
    }*/

