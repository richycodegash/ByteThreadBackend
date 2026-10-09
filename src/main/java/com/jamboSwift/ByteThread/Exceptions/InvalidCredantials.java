package com.jamboSwift.ByteThread.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidCredantials extends RuntimeException {
    public InvalidCredantials(String message) {
        super(message);
    }
}
