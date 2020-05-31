package com.volkovich.ImageStorageApplication.exception;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.ServletException;
import java.util.NoSuchElementException;

@ControllerAdvice
public class LogicExceptionProvider {

    @ExceptionHandler( LogicException.class )
    public ResponseEntity<Object> handleException(LogicException exception) {
        return new ResponseEntity<>(exception.getMessage(), exception.getStatus());
    }

    @ExceptionHandler( { EmptyResultDataAccessException.class, NoSuchElementException.class, ServletException.class } )
    public ResponseEntity<Object> noSuchException() {
        LogicException exception = new LogicException("Ошибка при работе с базой данных, наиболее вероятная причина - запрос несуществующего id");
        return new ResponseEntity<>(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
