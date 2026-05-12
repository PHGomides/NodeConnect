package com.nodeconnect.crypto;

import com.nodeconnect.core.models.MessageEnvelope;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

import static org.junit.jupiter.api.Assertions.*;

class AesGcmProviderTest {

    private AesGcmProvider provider;

    @BeforeEach
    void setUp() {
        // Chave AES-256 aleatória para cada teste
        byte[] keyBytes = new byte[32];
        new SecureRandom().nextBytes(keyBytes);
        SecretKey key = new SecretKeySpec(keyBytes, "AES");
        provider = new AesGcmProvider(key);
    }

    @Test
    void encryptThenDecryptShouldReturnOriginalMessage() throws Exception {
        String original = "Olá, mundo seguro!";

        MessageEnvelope envelope = provider.encrypt(original);
        String decrypted = provider.decrypt(envelope);

        assertEquals(original, decrypted);
    }

    @Test
    void encryptShouldFillAllRequiredFields() {
        MessageEnvelope envelope = provider.encrypt("teste");

        assertEquals("1.0", envelope.version());
        assertEquals("CHAT_MSG", envelope.type());
        assertTrue(envelope.timestamp() > 0);
        assertNotNull(envelope.payload());
        assertNotNull(envelope.nonce());
        assertFalse(envelope.payload().isEmpty());
        assertFalse(envelope.nonce().isEmpty());
    }

    @Test
    void twoEncryptionsOfSameMessageShouldHaveDifferentNonces() {
        MessageEnvelope first = provider.encrypt("mesma mensagem");
        MessageEnvelope second = provider.encrypt("mesma mensagem");

        assertNotEquals(first.nonce(), second.nonce());
        assertNotEquals(first.payload(), second.payload());
    }

    @Test
    void tamperedPayloadShouldThrowSecurityException() {
        MessageEnvelope original = provider.encrypt("mensagem importante");

        // Adultera o payload (troca o primeiro caractere)
        String tamperedPayload = "X" + original.payload().substring(1);
        MessageEnvelope tampered = new MessageEnvelope(
                original.version(),
                original.type(),
                original.timestamp(),
                tamperedPayload,
                original.nonce(),
                original.mac()
        );

        assertThrows(SecurityException.class, () -> provider.decrypt(tampered));
    }

    @Test
    void tamperedTimestampShouldThrowSecurityException() {
        // O timestamp está dentro da AAD, então alterá-lo deve quebrar a validação
        MessageEnvelope original = provider.encrypt("mensagem");

        MessageEnvelope tampered = new MessageEnvelope(
                original.version(),
                original.type(),
                original.timestamp() + 1000, // adulterado
                original.payload(),
                original.nonce(),
                original.mac()
        );

        assertThrows(SecurityException.class, () -> provider.decrypt(tampered));
    }

    @Test
    void decryptWithDifferentKeyShouldThrowSecurityException() {
        MessageEnvelope envelope = provider.encrypt("segredo");

        // Cria outro provider com chave diferente
        byte[] otherKeyBytes = new byte[32];
        new SecureRandom().nextBytes(otherKeyBytes);
        AesGcmProvider otherProvider = new AesGcmProvider(
                new SecretKeySpec(otherKeyBytes, "AES"));

        assertThrows(SecurityException.class, () -> otherProvider.decrypt(envelope));
    }
}