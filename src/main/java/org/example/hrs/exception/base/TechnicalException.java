package org.example.hrs.exception.base;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TechnicalException extends RuntimeException {
    private final HttpStatus httpStatus;

    public TechnicalException(HttpStatus httpStatus, Throwable e) {
        super(e.getMessage());
        this.httpStatus = httpStatus;
    }
}
