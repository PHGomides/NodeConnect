package com.nodeconnect.crypto;

import com.nodeconnect.core.models.MessageEnvelope;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.AEADBadTagException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;

public class AesGcmProvider implements SecurityService {

    private static final int NONCE_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final SecretKey sessionKey;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public AesGcmProvider(SecretKey sessionKey) {
        this.sessionKey = sessionKey;
    }

    @Override
    public MessageEnvelope encrypt(String plainText) {
        try {
            byte[] nonce = new byte[NONCE_LENGTH];
            SECURE_RANDOM.nextBytes(nonce);

            long timestamp = System.currentTimeMillis();

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, sessionKey,
                    new GCMParameterSpec(GCM_TAG_LENGTH, nonce));

            String aad = "1.0:CHAT_MSG:" + timestamp;
            cipher.updateAAD(aad.getBytes(StandardCharsets.UTF_8));

            byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            return new MessageEnvelope(
                    "1.0",
                    "CHAT_MSG",
                    timestamp,
                    Base64.getEncoder().encodeToString(cipherText),
                    Base64.getEncoder().encodeToString(nonce),
                    ""
            );
        } catch (Exception e) {
            throw new RuntimeException("Message encryption failed.", e);
        }
    }

    @Override
    public String decrypt(MessageEnvelope envelope) throws SecurityException {
        try {
            byte[] nonce = Base64.getDecoder().decode(envelope.nonce());
            byte[] cipherText = Base64.getDecoder().decode(envelope.payload());

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
            cipher.init(Cipher.DECRYPT_MODE, sessionKey,
                    new GCMParameterSpec(GCM_TAG_LENGTH, nonce));

            String aad = envelope.version() + ":" + envelope.type() + ":" + envelope.timestamp();
            cipher.updateAAD(aad.getBytes(StandardCharsets.UTF_8));

            return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);
        } catch (AEADBadTagException e) {
            throw new SecurityException("Invalid MAC - message rejected", e);
        } catch (Exception e) {
            throw new SecurityException("Decryption failed: " + e.getMessage(), e);
        }
    }
}