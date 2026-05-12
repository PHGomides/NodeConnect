package com.nodeconnect.crypto;

import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.HKDFBytesGenerator;
import org.bouncycastle.crypto.params.HKDFParameters;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;

public class EcdhKeyExchange {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static SecretKey deriveSharedSecret(PrivateKey privateKey, PublicKey publicKey) throws GeneralSecurityException{
        KeyAgreement agreement = KeyAgreement.getInstance("ECDH", "BC");
        agreement.init(privateKey);
        agreement.doPhase(publicKey, true);

        byte[] rawSecret = agreement.generateSecret();

        HKDFBytesGenerator hkdf = new HKDFBytesGenerator(new SHA256Digest());
        hkdf.init(new HKDFParameters(
                rawSecret,
                "nodeconnect-salt-v1".getBytes(StandardCharsets.UTF_8),
                "nodeconnect-aes256-session".getBytes(StandardCharsets.UTF_8)
        ));
        byte[] aesKey = new byte[32];
        hkdf.generateBytes(aesKey, 0, 32);
        return new SecretKeySpec(aesKey, "AES");
    }
}
