package com.nodeconnect.core.exceptions;

/**
 * Lançada quando tentamos usar o SecurityService antes do handshake ECDH ser concluído.
 */
public class HandshakeNotCompletedException extends RuntimeException {

    public HandshakeNotCompletedException(String message) {
        super(message);
    }
}