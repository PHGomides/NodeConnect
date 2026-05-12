package com.nodeconnect.crypto;

import com.nodeconnect.core.models.MessageEnvelope;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;

public class AesGcmProvider implements SecurityService {
    private static final int NONCE_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;

    private final SecretKey sessionKey;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public AesGcmProvider(SecretKey sessionKey) {
        this.sessionKey = sessionKey;
    }

    @Override
    public MessageEnvelope encrypt(String plainText) {
        try{
            byte[] nonce = new byte[NONCE_LENGTH];
            new SecureRandom().nextBytes(nonce);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, sessionKey, new GCMParameterSpec(GCM_TAG_LENGTH, nonce));

            byte[] cipherText = cipher.doFinal(plainText.getBytes());

            return new MessageEnvelope(
                    "1.0",
                    "CHAT_MSG",
                    System.currentTimeMillis(),
                    Base64.getEncoder().encodeToString(cipherText),
                    Base64.getEncoder().encodeToString(nonce),
                    ""
            );

        }catch (Exception e){
            throw new RuntimeException("Message encryption failed.", e);
        }
    }

    @Override
    public String decrypt(MessageEnvelope envelope) throws SecurityException {
        try {
            byte[] nonce = Base64.getDecoder().decode(envelope.nonce());
            byte[] cipherText = Base64.getDecoder().decode(envelope.payload());

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
            cipher.init(Cipher.DECRYPT_MODE, sessionKey, new GCMParameterSpec(GCM_TAG_LENGTH, nonce));

            return new String(cipher.doFinal(cipherText));
        } catch (Exception e){
            throw new SecurityException("Invalid MAC - message rejected");
        }
    }
}