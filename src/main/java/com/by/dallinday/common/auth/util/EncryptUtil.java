package com.by.dallinday.common.auth.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class EncryptUtil {

    @Value("${aes.secret-key}")
    private String KEY; // 32byte

    public String encrypt(String strToEncrypt) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            throw new RuntimeException("Error while encrypting: " + e.toString());
        }
    }

    public String decrypt(String strToDecrypt) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            throw new RuntimeException("Error while decrypting: " + e.toString());
        }
    }
}