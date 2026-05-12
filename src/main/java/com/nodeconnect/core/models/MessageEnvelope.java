package com.nodeconnect.core.models;

import java.io.Serializable;

/**
 * Representa o envelope de segurança que trafega na rede.
 **/
public record MessageEnvelope(
        String version,
        String type,
        String payload,
        String nonce,
        String mac
) implements Serializable {}