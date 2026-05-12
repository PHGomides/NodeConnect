package com.nodeconnect.app;

import com.nodeconnect.crypto.HandshakeService;
import com.nodeconnect.crypto.KeyManager;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("=== NodeConnect ===");

        KeyManager keyManager = new KeyManager();
        HandshakeService handshake = new HandshakeService(keyManager);

        System.out.println("Sua chave pública:");
        System.out.println(handshake.getLocalPublicKeyBase64());

        // TODO: integração com a camada de rede
    }
}