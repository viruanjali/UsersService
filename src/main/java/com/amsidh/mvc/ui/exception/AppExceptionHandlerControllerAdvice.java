package com.amsidh.mvc.ui.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class AppExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDto> handleAnyException(
            Exception exception, WebRequest request) {
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto();
        exceptionResponseDto.setDate(now());
        exceptionResponseDto.setMessage(ofNullable(exception.getLocalizedMessage()).orElse("No exception message found"));
        return new ResponseEntity<>(exceptionResponseDto, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ExceptionResponseDto> handleNullPointerException(
            NullPointerException exception, WebRequest request) {
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto();
        exceptionResponseDto.setDate(now());
        exceptionResponseDto.setMessage(ofNullable(exception.getLocalizedMessage()).orElse("No exception message found"));
        return new ResponseEntity<>(exceptionResponseDto, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {UserNotFoundException.class, NoDataFoundException.class})
    public ResponseEntity<ExceptionResponseDto> handleNotFoundException(
            Exception exception, WebRequest request) {
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto();
        exceptionResponseDto.setDate(now());
        exceptionResponseDto.setMessage(ofNullable(exception.getLocalizedMessage()).orElse("No exception message found"));
        return new ResponseEntity<>(exceptionResponseDto, NOT_FOUND);
    }

    @ExceptionHandler(value = {DuplicateUserException.class})
    public ResponseEntity<ExceptionResponseDto> handleDataAlreadyExistsException(
            Exception exception, WebRequest request) {
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto();
        exceptionResponseDto.setDate(now());
        exceptionResponseDto.setMessage(ofNullable(exception.getLocalizedMessage()).orElse("No exception message found"));
        return new ResponseEntity<>(exceptionResponseDto, CONFLICT);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto();
        exceptionResponseDto.setDate(now());
        exceptionResponseDto.setStatus(status.value());

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .collect(toList());
        exceptionResponseDto.setErrors(errors);

        return new ResponseEntity<>(exceptionResponseDto, BAD_REQUEST);
    }
}
