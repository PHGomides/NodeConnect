package com.nodeconnect.crypto;

import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;

public class EcdhKeyExchange {
    public static SecretKey deriveSharedSecret(PrivateKey privateKey, PublicKey publicKey) throws GeneralSecurityException{
        KeyAgreement agreement = KeyAgreement.getInstance("ECDH", "BC");
        agreement.init(privateKey);
        agreement.doPhase(publicKey, true);

        byte[] rawSecret = agreement.generateSecret();

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] aesKey = digest.digest(rawSecret);

        return new SecretKeySpec(aesKey, "AES");
    }
}
