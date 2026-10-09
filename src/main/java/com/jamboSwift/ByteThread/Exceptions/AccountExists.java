package com.jamboSwift.ByteThread.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AccountExists extends RuntimeException {
    public AccountExists(String message) {
        super(message);
    }
}
