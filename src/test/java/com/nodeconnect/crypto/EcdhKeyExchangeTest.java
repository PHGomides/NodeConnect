package com.nodeconnect.crypto;

import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.*;

class EcdhKeyExchangeTest {

    @Test
    void twoNodesShouldDeriveTheSameSharedSecret() throws Exception {
        // Simula os dois lados do handshake
        KeyManager alice = new KeyManager();
        KeyManager bob = new KeyManager();

        // Cada um deriva o segredo usando a chave pública do outro
        SecretKey aliceSecret = alice.deriveSharedSecret(bob.getLocalPublicKey());
        SecretKey bobSecret = bob.deriveSharedSecret(alice.getLocalPublicKey());

        assertArrayEquals(aliceSecret.getEncoded(), bobSecret.getEncoded(),
                "Os dois nós devem derivar exatamente a mesma chave AES");
    }

    @Test
    void derivedKeyShouldBe256Bits() throws Exception {
        KeyManager alice = new KeyManager();
        KeyManager bob = new KeyManager();

        SecretKey shared = alice.deriveSharedSecret(bob.getLocalPublicKey());

        assertEquals(32, shared.getEncoded().length, "Chave deve ter 32 bytes (256 bits)");
        assertEquals("AES", shared.getAlgorithm());
    }

    @Test
    void differentKeyPairsShouldProduceDifferentSecrets() throws Exception {
        KeyManager alice = new KeyManager();
        KeyManager bob = new KeyManager();
        KeyManager charlie = new KeyManager();

        SecretKey aliceBob = alice.deriveSharedSecret(bob.getLocalPublicKey());
        SecretKey aliceCharlie = alice.deriveSharedSecret(charlie.getLocalPublicKey());

        assertFalse(java.util.Arrays.equals(
                        aliceBob.getEncoded(), aliceCharlie.getEncoded()),
                "Pares diferentes devem gerar segredos diferentes");
    }
}