package com.storefront.user.security.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class RsaKeyLoader {
	
	private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;

    public RsaKeyLoader(JwtProperties properties,  ResourceLoader resourceLoader) {
        try {
            this.privateKey = loadPrivateKey(resourceLoader.getResource(properties.getPrivateKeyLocation()));
            this.publicKey = loadPublicKey(resourceLoader.getResource(properties.getPublicKeyLocation()));
        }
        catch (Exception ex) {
            throw new IllegalStateException("Unable to load RSA key pair.", ex);
        }
    }

    private RSAPrivateKey loadPrivateKey(Resource resource) throws Exception {
        String key = read(resource);
        key = key.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "").replaceAll("\\s", "");
        byte[] decoded = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(spec);
    }

    private RSAPublicKey loadPublicKey(Resource resource) throws Exception {
        String key = read(resource);
        key = key.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "").replaceAll("\\s", "");
        byte[] decoded = Base64.getDecoder().decode(key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(spec);
    }

    private String read(Resource resource) throws IOException {
    	try (InputStream inputStream = resource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
    
    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }

    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

}