package com.emijordan.Spotinsights.service.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final TextEncryptor encryptor;
    private String salt = KeyGenerators.string().generateKey();

    @Autowired
    public TokenService(@Value("${encryption.password}") String password) {
        this.encryptor = Encryptors.delux(password, salt);
    }

    public String encryptToken(String token) {
        return encryptor.encrypt(token);
    }
    public String decryptToken(String tokenEncriptado) {
        return encryptor.decrypt(tokenEncriptado);
    }
}
