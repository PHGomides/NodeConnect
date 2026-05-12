package com.nodeconnect.crypto;

import javax.crypto.SecretKey;
import java.security.*;
import java.util.Base64;

public class HandshakeService {

    private final KeyManager keyManager;
    private SecurityService securityService;

    public HandshakeService(KeyManager keyManager) {
        this.keyManager = keyManager;
    }

    public String getLocalPublicKeyBase64() {
        return Base64.getEncoder()
                .encodeToString(keyManager.getLocalPublicKey().getEncoded());
    }

    public void receivePeerPublicKey(String base64PeerKey)
            throws GeneralSecurityException {
        byte[] keyBytes = Base64.getDecoder().decode(base64PeerKey);

        KeyFactory factory = KeyFactory.getInstance("EC", "BC");
        PublicKey peerKey = factory.generatePublic(
                new java.security.spec.X509EncodedKeySpec(keyBytes));

        keyManager.setPeerPublicKey(peerKey);

        SecretKey sharedKey = EcdhKeyExchange.deriveSharedSecret(
                keyManager.getLocalPrivateKey(),
                keyManager.getPeerPublicKey());

        this.securityService = new AesGcmProvider(sharedKey);
    }

    public SecurityService getSecurityService() {
        if (securityService == null) {
            throw new IllegalStateException("Handshake not completed");
        }
        return securityService;
    }
}