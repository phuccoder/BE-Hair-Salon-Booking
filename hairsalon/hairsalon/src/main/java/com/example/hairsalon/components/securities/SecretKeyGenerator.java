package com.example.hairsalon.components.securities;

import java.security.SecureRandom;
import java.util.Base64;

public class SecretKeyGenerator {
    public static void main(String[] args) {
        SecureRandom random = new SecureRandom();
        byte[] secretKey = new byte[32];  // 256-bit key
        random.nextBytes(secretKey);

        // Base64 encode the secret
        String encodedSecret = Base64.getEncoder().encodeToString(secretKey);

        System.out.println("Generated secret: " + encodedSecret);
    }
}