package com.marcos.desenvolvimento.authorization_ms.service;

import com.marcos.desenvolvimento.authorization_ms.dto.request.RegisterRequest;
import com.marcos.desenvolvimento.authorization_ms.dto.response.KeycloakUserDTO;
import com.marcos.desenvolvimento.authorization_ms.exception.InternalServerErrorException;
import com.marcos.desenvolvimento.authorization_ms.exception.InvalidUserCreationException;
import com.marcos.desenvolvimento.authorization_ms.exception.UserExistsException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.stereotype.Service;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;

@Slf4j
@Service
public class KeycloakService {

    @Value(value = "${keycloak.auth-server-url}")
    private String keycloakServerUrl;

    @Value(value = "${keycloak.realm}")
    private String realm;

    @Value(value = "${keycloak.admin.client-id}")
    private String clientId;

    @Value(value = "${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String clientSecret;

    @Value(value = "${keycloak.admin.username}")
    private String adminUsername;

    @Value(value = "${keycloak.admin.password}")
    private String adminPassword;

    private Keycloak getKeycloakInstance() {
        return KeycloakBuilder.builder()
                .serverUrl(keycloakServerUrl)
                .realm("master")
                .clientId("admin-cli")
                .username(adminUsername)
                .password(adminPassword)
                .grantType(OAuth2Constants.PASSWORD)
                .build();
    }

    public void createUser(final RegisterRequest request) {
        Keycloak keycloak = getKeycloakInstance();
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        UserRepresentation user = new UserRepresentation();

        validateUserCreationInput(request);

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setEnabled(true);
        user.setEmailVerified(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(request.password());
        user.setCredentials(Collections.singletonList(credential));

        Response response = usersResource.create(user);

        if(response.getStatus() == 409){
            throw new UserExistsException("User already exists.");
        }

        if(response.getStatus() == 500){
            throw new InternalServerErrorException("An error ocurred while trying to create an user. Contact the administrator.");
        }

        if(response.getStatus() == 201){
            log.info("Creating user {}...", request.username());
        }

    }

    private void validateUserCreationInput(final RegisterRequest registerRequest) {
        if ((registerRequest.username() == null || registerRequest.username().isEmpty()) && (registerRequest.email() == null || registerRequest.email().isEmpty())) {
            throw new InvalidUserCreationException("The login must be set. Email or username.");
        }
        if (registerRequest.password() == null || registerRequest.password().isEmpty()) {
            throw new InvalidUserCreationException("The password cannot be null or empty.");
        }
        if ((registerRequest.firstName() == null || registerRequest.firstName().isEmpty()) && (registerRequest.lastName() == null || registerRequest.lastName().isEmpty())) {
            throw new InvalidUserCreationException("The user first name cannot be null or empty.");
        }
    }

    public List<KeycloakUserDTO> findAll() {
        List<KeycloakUserDTO> userDTOs = new ArrayList<>();
        try {
            Keycloak keycloak = getKeycloakInstance();
            RealmResource realmResource = keycloak.realm(realm);
            UsersResource usersResource = realmResource.users();

            List<UserRepresentation> userRepresentations = usersResource.list();


            for (UserRepresentation user : userRepresentations) {
                KeycloakUserDTO dto = new KeycloakUserDTO(
                        user.getUsername(),
                        user.getEmail(),
                        user.getFirstName(),
                        user.getLastName()
                );
                userDTOs.add(dto);
            }

        } catch (Exception e) {
            log.error("Failed to fech Keycloak user: {}", e.getMessage(), e);
        }
        return userDTOs;
    }
}
