package com.google.api.services.samples.keycloak.cmdline;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PKCE {
    private final String verifier;
    private final String challenge;
    private String challengeMethod;

    public PKCE() {
        verifier = generateVerifier();
        Challenge c = new Challenge(verifier);
        challenge = c.challenge;
        challengeMethod = c.method;
    }

    private String generateVerifier() {
        SecureRandom sr = new SecureRandom();
        byte[] code = new byte[32];
        sr.nextBytes(code);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(code);
    }

    public String getVerifier() {
        return verifier;
    }

    public String getChallenge() {
        return challenge;
    }

    public String getChallengeMethod() {
        return challengeMethod;
    }

    private class Challenge {
        private String challenge;
        private String method;

        private Challenge(String verifier) {
            try {
                byte[] bytes = verifier.getBytes();
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(bytes, 0, bytes.length);
                byte[] digest = md.digest();
                challenge = Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
                method = "S256";
            } catch (NoSuchAlgorithmException e) {
                challenge = verifier;
            }
        }
    }
}
