package com.nodeconnect.core.exceptions;

/**
 * Lançada quando a decifragem falha por qualquer motivo:
 * MAC inválido, payload adulterado, base64 malformado, etc.
 */
public class DecryptionFailedException extends Exception {

    public DecryptionFailedException(String message) {
        super(message);
    }

    public DecryptionFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}