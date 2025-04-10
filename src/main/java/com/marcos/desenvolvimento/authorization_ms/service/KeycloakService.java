package com.marcos.desenvolvimento.authorization_ms.service;

import com.marcos.desenvolvimento.authorization_ms.exception.InternalServerErrorException;
import com.marcos.desenvolvimento.authorization_ms.exception.InvalidUserCreationException;
import com.marcos.desenvolvimento.authorization_ms.exception.UserExistsException;
import jakarta.ws.rs.core.Response;
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

    public void createUser(String username, String password, String email, String firstName, String lastName) {
        Keycloak keycloak = getKeycloakInstance();
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        UserRepresentation user = new UserRepresentation();

        validateUserCreationInput(username, password, email, firstName, lastName);

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setEmail(email);
        user.setEnabled(true);
        user.setEmailVerified(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        user.setCredentials(Collections.singletonList(credential));

        Response response = usersResource.create(user);
        int status = response.getStatus();

        switch(status){
            case 409 -> throw new UserExistsException("User already exists.");
            case 500 -> throw new InternalServerErrorException("An error ocurred while trying to create an user. Contact the administrator.");
            default -> throw new InternalServerErrorException("An unhandled inaccuracy occurred. Please contact the system administrator.");
        }
    }

    private void validateUserCreationInput(String username, String password, String email, String firstName, String lastName) {
        if ((username == null || username.isEmpty()) && (email == null || email.isEmpty())) {
            throw new InvalidUserCreationException("The login must be set. Email or username.");
        }
        if (password == null || password.isEmpty()) {
            throw new InvalidUserCreationException("The password cannot be null or empty.");
        }
        if ((firstName == null || firstName.isEmpty()) && (lastName == null || lastName.isEmpty())) {
            throw new InvalidUserCreationException("The user first name cannot be null or empty.");
        }
    }

}
