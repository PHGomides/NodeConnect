package com.nodeconnect.crypto;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;

import javax.crypto.SecretKey;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.Security;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;


public class KeyManager {
    private static final String CURVE_NAME = "secp256r1";

    private final KeyPair localKeyPair;
    private PublicKey peerPublicKey;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private KeyPair generateKeyPair() throws GeneralSecurityException {
        ECNamedCurveParameterSpec spec = ECNamedCurveTable.getParameterSpec(CURVE_NAME);
        KeyPairGenerator gen = KeyPairGenerator.getInstance("EC", "BC");
        gen.initialize(spec);
        return gen.generateKeyPair();
    }

    public KeyManager() throws GeneralSecurityException {
        this.localKeyPair = generateKeyPair();
    }

    public  PublicKey getLocalPublicKey() {
        return localKeyPair.getPublic();
    }

    public SecretKey deriveSharedSecret(PublicKey peerPublicKey)
            throws GeneralSecurityException {
        return EcdhKeyExchange.deriveSharedSecret(
                localKeyPair.getPrivate(), peerPublicKey);
    }

    public void setPeerPublicKey(PublicKey peerPublicKey) {
        this.peerPublicKey = peerPublicKey;
    }

    public PublicKey getPeerPublicKey() {
        return peerPublicKey;
    }

    public boolean isHandshakeComplete() {
        return peerPublicKey != null;
    }

}
