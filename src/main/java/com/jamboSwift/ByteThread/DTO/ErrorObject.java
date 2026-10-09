package com.jamboSwift.ByteThread.DTO;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorObject(
        int errorStatus,
        String errorType,
        LocalDateTime timestamp,
        String error,
        String copyright
) {
    private static final String DEFAULT_COPYRIGHT = "© " + LocalDateTime.now().getYear() + " JamboSwift. All rights reserved.";

    // Canonical Compact Constructor
    public ErrorObject {
        if (copyright == null || copyright.isBlank()) {
            copyright = DEFAULT_COPYRIGHT;
        }
    }

    // Overloaded Constructor (omits copyright argument)
    public ErrorObject(int errorStatus, String errorType, LocalDateTime timestamp, String error) {
        this(errorStatus, errorType, timestamp, error, DEFAULT_COPYRIGHT);
    }
}
