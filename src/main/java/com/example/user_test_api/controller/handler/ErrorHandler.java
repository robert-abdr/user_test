package com.example.user_test_api.controller.handler;

import com.example.user_test_api.dto.ErrorResponseDto;
import com.example.user_test_api.exception.UserExistException;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ErrorHandler {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleNotFound(EntityNotFoundException e) {
        log.error("EntityNotFoundException was thrown", e);
        return new ErrorResponseDto(
                HttpStatus.NOT_FOUND.name(),
                "The required object was not found.",
                e.getMessage(),
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler(IllegalAccessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleIllegalArgument(Exception e) {
        log.error("Illegal argument", e);
        return new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.name(),
                "Illegal arguments",
                e.getMessage(),
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler({
        MethodArgumentNotValidException.class,
        FileSizeLimitExceededException.class,
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleBadRequestExceptions(Exception e) {
        log.error("{}  was thrown", e.getClass().getSimpleName(), e);
        return new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.name(),
                "Incorrectly made request.",
                e.getMessage(),
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler(UserExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseDto handleUserExistException(Exception e) {
        log.error("User already exist.", e);
        return new ErrorResponseDto(
                HttpStatus.CONFLICT.name(),
                "User already exist.",
                e.getMessage(),
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDto handleAllExceptions(Exception e) {
        log.error("Internal server error.", e);
        return new ErrorResponseDto(
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                "Something gone wrong.",
                e.getMessage(),
                LocalDateTime.now().format(formatter));
    }
}
