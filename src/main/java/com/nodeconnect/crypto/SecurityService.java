package com.nodeconnect.crypto;

import com.nodeconnect.core.exceptions.DecryptionFailedException;
import com.nodeconnect.core.models.MessageEnvelope;

public interface SecurityService {
    MessageEnvelope encrypt(String plainText);
    String decrypt(MessageEnvelope envelope) throws DecryptionFailedException;
}