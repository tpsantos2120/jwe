package com.example.jwe;

import io.fusionauth.jwks.domain.JSONWebKey;
import io.fusionauth.jwt.JWTUtils;
import io.fusionauth.jwt.Signer;
import io.fusionauth.jwt.Verifier;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.domain.KeyPair;
import io.fusionauth.jwt.ec.ECSigner;
import io.fusionauth.jwt.ec.ECVerifier;
import io.fusionauth.jwt.hmac.HMACSigner;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Base64;

@Component
public class JWTExample {

    public void runJWT() throws NoSuchAlgorithmException, InvalidKeySpecException {
        // encrypt a JWT
//        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
//        keyPairGenerator.initialize(2048);
//        KeyPair keyPair = keyPairGenerator.generateKeyPair();
//
//        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        RSAPublicKeySpec publicKeySpec = keyFactory.getKeySpec(keyPair.getPublic(), RSAPublicKeySpec.class);
//        RSAPrivateKeySpec privateKeySpec = keyFactory.getKeySpec(keyPair.getPrivate(), RSAPrivateKeySpec.class);
//
//        RSAPublicKey publicRsaKey = (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
//        RSAPrivateKey privateRsaKey = (RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);

        KeyPair keys = JWTUtils.generate521_ECKeyPair();

        System.out.println("Public Key: " + Base64.getEncoder().encodeToString(keys.publicKey.getBytes()));
        System.out.println("Private Key: " + Base64.getEncoder().encodeToString(keys.privateKey.getBytes()));

        Signer signer = ECSigner.newSHA512Signer(keys.privateKey);

        JWT jwt = new JWT().setIssuer("issuer")
                .setIssuedAt(ZonedDateTime.now(ZoneOffset.UTC))
                .setSubject("f1e33ab3-027f-47c5-bb07-8dd8ab37a2d3")
                .setExpiration(ZonedDateTime.now(ZoneOffset.UTC).plusMinutes(60));

        String encodedJWT = JWT.getEncoder().encode(jwt, signer);
        System.out.println("JWT: [" + encodedJWT + "]");

        // decrypt
        Verifier verifier = ECVerifier.newVerifier(keys.publicKey);
        JWT decodedJWT = JWT.getDecoder().decode(encodedJWT, verifier);

        System.out.println("Subject: [" + decodedJWT.subject + "]");
        System.out.println("IssuedAt: [" + decodedJWT.issuedAt + "]");
        System.out.println("Expiration: [" + decodedJWT.expiration + "]");

        JSONWebKey jwk = JSONWebKey.build(keys.publicKey);
        System.out.println("Token: [" + jwk + "]");
    }
}
