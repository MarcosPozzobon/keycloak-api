package com.marcos.desenvolvimento.authorization_ms.service;

import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Map;

@Service
public class TokenService {

    @Value(value = "${jwks.url.validator}")
    private String JWKS_URL;

    public JWTClaimsSet getClaims(String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isTokenValido(String authorizationHeader) {
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
