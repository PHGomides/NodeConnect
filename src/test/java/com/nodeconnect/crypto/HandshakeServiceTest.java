package com.nodeconnect.crypto;

import com.nodeconnect.core.models.MessageEnvelope;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HandshakeServiceTest {

    @Test
    void fullHandshakeShouldEnableBidirectionalCommunication() throws Exception {
        // Simula Alice e Bob completando o handshake
        KeyManager aliceKeys = new KeyManager();
        KeyManager bobKeys = new KeyManager();

        HandshakeService aliceHandshake = new HandshakeService(aliceKeys);
        HandshakeService bobHandshake = new HandshakeService(bobKeys);

        // Troca de chaves públicas (simulando o envio pela rede)
        String alicePubKey = aliceHandshake.getLocalPublicKeyBase64();
        String bobPubKey = bobHandshake.getLocalPublicKeyBase64();

        aliceHandshake.receivePeerPublicKey(bobPubKey);
        bobHandshake.receivePeerPublicKey(alicePubKey);

        // Alice cifra e Bob decifra
        SecurityService aliceCrypto = aliceHandshake.getSecurityService();
        SecurityService bobCrypto = bobHandshake.getSecurityService();

        String mensagemOriginal = "Mensagem secreta para o Bob!";
        MessageEnvelope envelope = aliceCrypto.encrypt(mensagemOriginal);
        String mensagemRecebida = bobCrypto.decrypt(envelope);

        assertEquals(mensagemOriginal, mensagemRecebida);
    }

    @Test
    void getSecurityServiceBeforeHandshakeShouldThrow() throws Exception {
        HandshakeService handshake = new HandshakeService(new KeyManager());

        assertThrows(IllegalStateException.class,
                handshake::getSecurityService);
    }
}