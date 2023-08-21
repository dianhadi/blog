package com.dianhadi.blog.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;

public class TokenGenerator {
    private static RSAPrivateKey privateKey;

    static {
        try {
            File privateKeyFile = new File("/var/files/private_key.pem"); // Update the path
            privateKey = readPrivateKey(privateKeyFile);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error initializing private key");
        }
    }

    public static String generateToken() {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // System.out.println(privateKey);
        // return "";

        return Jwts.builder()
                .claim("service", "blog-service")
                .setIssuedAt(now)
                .setExpiration(new Date(nowMillis + 3600000)) // 1 hour expiration
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    private static RSAPrivateKey readPrivateKey(File file) throws Exception {
        String key = new String(Files.readAllBytes(file.toPath()), Charset.defaultCharset());

        String privateKeyPEM = key
        .replace("-----BEGIN PRIVATE KEY-----", "")
        .replaceAll(System.lineSeparator(), "")
        .replace("-----END PRIVATE KEY-----", "");

        byte[] encoded = Base64.decodeBase64(privateKeyPEM);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }
}
