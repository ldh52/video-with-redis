package com.example.mytv.adapter.in.api.advice;

import com.example.mytv.adapter.in.api.dto.ErrorResponse;
import com.example.mytv.exception.BadRequestException;
import com.example.mytv.exception.DomainNotFoundException;
import com.example.mytv.exception.ForbiddenRequestException;
import com.example.mytv.exception.UnauthorizedException;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(Exception ex) {
        return ErrorResponse.builder()
            .type("badRequest")
            .detail(ex.getMessage())
            .timestamp(LocalDateTime.now())
            .build();
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleUnauthorized(Exception ex) {
        return ErrorResponse.builder()
            .type("unauthorized")
            .detail(ex.getMessage())
            .timestamp(LocalDateTime.now())
            .build();
    }
    @ExceptionHandler(ForbiddenRequestException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbidden(Exception ex) {
        return ErrorResponse.builder()
            .type("forbidden")
            .detail(ex.getMessage())
            .timestamp(LocalDateTime.now())
            .build();
    }

    @ExceptionHandler(DomainNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleDomainNotFound(Exception ex) {
        return ErrorResponse.builder()
            .type("notFound")
            .detail(ex.getMessage())
            .timestamp(LocalDateTime.now())
            .build();
    }
}
