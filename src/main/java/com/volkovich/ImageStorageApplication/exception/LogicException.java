package com.volkovich.ImageStorageApplication.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class LogicException extends Exception {

    @Getter
    private HttpStatus status;
    @Getter
    private String message;

    public LogicException(String message) {
        this.message = message;
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
