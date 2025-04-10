package com.marcos.desenvolvimento.authorization_ms.service;

import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.interfaces.RSAPublicKey;


@Service
public class TokenService {

    private static final String JWKS_URL = "http://localhost:8080/realms/DESENVOLVIMENTO/protocol/openid-connect/certs";

    public boolean isValidToken(String authorizationHeader) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");
            SignedJWT signedJWT = SignedJWT.parse(token);

            RSAPublicKey publicKey = getPublicKey(signedJWT);

            if (publicKey == null) {
                return false;
            }
            JWSVerifier verifier = new RSASSAVerifier(publicKey);
            return signedJWT.verify(verifier);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private RSAPublicKey getPublicKey(SignedJWT signedJWT) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String jwkSetJson = restTemplate.getForObject(JWKS_URL, String.class);

        JWKSet jwkSet = JWKSet.parse(jwkSetJson);

        JWK jwk = jwkSet.getKeyByKeyId(signedJWT.getHeader().getKeyID());

        if (jwk instanceof RSAKey rsaKey) {
            return rsaKey.toRSAPublicKey();
        }
        return null;
    }
}
