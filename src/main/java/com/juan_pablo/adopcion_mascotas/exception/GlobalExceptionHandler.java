package com.juan_pablo.adopcion_mascotas.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends RuntimeException  {

    @ExceptionHandler({
            Exception.class,
            ObjectNotFoundException.class,
            MethodArgumentTypeMismatchException.class,
            MethodArgumentNotValidException.class,
            HttpRequestMethodNotSupportedException.class,
            HttpMessageNotReadableException.class,
    })

    public ResponseEntity<ApiError> handleAllExceptions(Exception exception,
                                                        HttpServletRequest request,
                                                        HttpServletResponse response) {
        ZoneId zoneId = ZoneId.of("America/La_Paz");
        LocalDateTime timestamp = LocalDateTime.now(zoneId);

        log.error("Exception caught: [{}] - Message: [{}] - URL: [{}] - Method: [{}]",
                exception.getClass().getSimpleName(),
                exception.getMessage(),
                request.getRequestURL(),
                request.getMethod());

        if (exception instanceof ObjectNotFoundException objectNotFoundException) {
            return this.handleObjectNotFoundException(objectNotFoundException, request, response, timestamp);
        } else if (exception instanceof MethodArgumentTypeMismatchException methodArgumentTypeMismatchException) {
            return this.handleMethodArgumentTypeMismatchException(methodArgumentTypeMismatchException, request, response, timestamp);
        } else if (exception instanceof MethodArgumentNotValidException methodArgumentNotValidException) {
            return this.handleMethodArgumentNotValidException(methodArgumentNotValidException, request, response, timestamp);
        } else if (exception instanceof HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException) {
            return this.handleHttpRequestMethodNotSupportedException(httpRequestMethodNotSupportedException, request, response, timestamp);
        } else if (exception instanceof HttpMessageNotReadableException httpMessageNotReadableException) {
            return this.handleHttpMessageNotReadableException(httpMessageNotReadableException, request, response, timestamp);
        } else if (exception instanceof UsernameNotFoundException usernameNotFoundException) {
            return this.handleUsernameNotFoundException(usernameNotFoundException, request, response, timestamp);
        } else {
            return this.handleException(exception, request, response, timestamp);

        }


    }

    private ResponseEntity<ApiError> handleObjectNotFoundException(ObjectNotFoundException objectNotFoundException,
                                                                   HttpServletRequest request,
                                                                   HttpServletResponse response,
                                                                   LocalDateTime timestamp) {
        log.warn("ObjectNotFoundException: [{}] - URL: [{}] - Method: [{}]",
                objectNotFoundException.getMessage(), request.getRequestURL(), request.getMethod());
        int httpStatus = HttpStatus.NOT_FOUND.value();
        ApiError apiError = new ApiError(
                httpStatus,
                request.getRequestURL().toString(),
                request.getMethod(),
                "I'm sorry, the requested information could not be found. " +
                        "Please check the URL or try another search.",
                objectNotFoundException.getMessage(),
                timestamp,
                null
        );
        return ResponseEntity.status(httpStatus).body(apiError);
    }

    private ResponseEntity<ApiError> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException methodArgumentTypeMismatchException,
                                                                               HttpServletRequest request,
                                                                               HttpServletResponse response,
                                                                               LocalDateTime timestamp) {

        log.error("MethodArgumentTypeMismatchException: [{}] - Rejected Value: [{}] - URL: [{}] - Method: [{}]",
                methodArgumentTypeMismatchException.getMessage(), methodArgumentTypeMismatchException.getValue(), request.getRequestURL(), request.getMethod());

        int httpStatus = HttpStatus.BAD_REQUEST.value();

        Object valueRejected = methodArgumentTypeMismatchException.getValue();
        String propertyName = methodArgumentTypeMismatchException.getName();

        ApiError apiError = new ApiError(
                httpStatus,
                request.getRequestURL().toString(),
                request.getMethod(),
                "Invalid Request: The provided value " + valueRejected + " does not have the expected type for the " + propertyName,
                methodArgumentTypeMismatchException.getMessage(),
                timestamp,
                null
        );

        return ResponseEntity.status(httpStatus).body(apiError);
    }

    private ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException,
                                                                           HttpServletRequest request,
                                                                           HttpServletResponse response,
                                                                           LocalDateTime timestamp) {

        log.error("MethodArgumentNotValidException: [{}] - URL: [{}] - Method: [{}]",
                methodArgumentNotValidException.getMessage(), request.getRequestURL(), request.getMethod());
        int httpStatus = HttpStatus.BAD_REQUEST.value();

        List<ObjectError> errors = methodArgumentNotValidException.getAllErrors();
        List<String> details = errors.stream()
                .map(error -> {
                    if (error instanceof FieldError fieldError) {
                        return fieldError.getField() + " " + fieldError.getDefaultMessage();
                    }
                    return error.getDefaultMessage();
                }).toList();

        ApiError apiError = new ApiError(
                httpStatus,
                request.getRequestURL().toString(),
                request.getMethod(),
                "The request contains invalid or incomplete parameters. " +
                        "Please verify and provide the required information before trying again.",
                methodArgumentNotValidException.getMessage(),
                timestamp,
                details
        );
        return ResponseEntity.status(httpStatus).body(apiError);
    }

    private ResponseEntity<ApiError> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException,
                                                                                  HttpServletRequest request,
                                                                                  HttpServletResponse response,
                                                                                  LocalDateTime timestamp) {
        log.error("HttpRequestMethodNotSupportedException: [{}] - URL: [{}] - Method: [{}]",
                httpRequestMethodNotSupportedException.getMessage(), request.getRequestURL(), request.getMethod());

        int httpStatus = HttpStatus.METHOD_NOT_ALLOWED.value();
        ApiError apiError = new ApiError(
                httpStatus,
                request.getRequestURL().toString(),
                request.getMethod(),
                "Oops! Method Not Allowed. Check the HTTP method of your request.",
                httpRequestMethodNotSupportedException.getMessage(),
                timestamp,
                null
        );

        return ResponseEntity.status(httpStatus).body(apiError);

    }

    private ResponseEntity<ApiError> handleHttpMessageNotReadableException(HttpMessageNotReadableException httpMessageNotReadableException,
                                                                           HttpServletRequest request,
                                                                           HttpServletResponse response,
                                                                           LocalDateTime timestamp) {

        log.error("HttpMessageNotReadableException: [{}] - URL: [{}] - Method: [{}]",
                httpMessageNotReadableException.getMessage(), request.getRequestURL(), request.getMethod());

        int httpStatus = HttpStatus.BAD_REQUEST.value();
        ApiError apiError = new ApiError(
                httpStatus,
                request.getRequestURL().toString(),
                request.getMethod(),
                "Oops! Error reading the HTTP message body. " +
                        "Make sure the request is correctly formatted and contains valid data.",
                httpMessageNotReadableException.getMessage(),
                timestamp,
                null
        );

        return ResponseEntity.status(httpStatus).body(apiError);

    }

    private ResponseEntity<ApiError> handleUsernameNotFoundException(UsernameNotFoundException  usernameNotFoundException ,
                                                                           HttpServletRequest request,
                                                                           HttpServletResponse response,
                                                                           LocalDateTime timestamp) {

        log.error("UsernameNotFoundException: [{}] - URL: [{}] - Method: [{}]",
                usernameNotFoundException.getMessage(), request.getRequestURL(), request.getMethod());

        int httpStatus = HttpStatus.BAD_REQUEST.value();
        ApiError apiError = new ApiError(
                httpStatus,
                request.getRequestURL().toString(),
                request.getMethod(),
                "User not found",
                usernameNotFoundException.getMessage(),
                timestamp,
                null
        );

        return ResponseEntity.status(httpStatus).body(apiError);

    }

    private ResponseEntity<ApiError> handleException(Exception exception,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response,
                                                     LocalDateTime timestamp) {
        log.error("Unhandled Exception: [{}] - URL: [{}] - Method: [{}]",
                exception.getMessage(), request.getRequestURL(), request.getMethod());


        int httpStatus = HttpStatus.INTERNAL_SERVER_ERROR.value();
        ApiError apiError = new ApiError(
                httpStatus,
                request.getRequestURL().toString(),
                request.getMethod(),
                "Oops! Something went wrong on our server. Please try again later.",
                exception.getMessage(),
                timestamp,
                null
        );

        return ResponseEntity.status(httpStatus).body(apiError);
    }
}
