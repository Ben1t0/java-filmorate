package ru.yandex.practicum.filmorate.controller;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.ValidationErrorResponse;
import ru.yandex.practicum.filmorate.model.Violation;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorHandlingControllerAdvice {
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onConstraintValidationException(ConstraintViolationException e) {
        final List<Violation> violations = e.getConstraintViolations().stream()
                .map(
                        violation -> new Violation(
                                violation.getPropertyPath().toString(),
                                violation.getMessage()
                        )
                )
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        final List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String onEntityAlreadyExistsException(AlreadyExistsException e) {
        if (e.getCause() != null) {
            return "{\n  \"error\": \"" + e.getMessage() + ", \n\"cause\": \"" + e.getCause().getMessage() + "\" \"\n}";
        } else {
            return "{\n  \"error\": \"" + e.getMessage() + "\"\n}";
        }
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Violation onEntityValidationException(ValidationException e) {
        return new Violation(e.getFieldName(), e.getMessage());
    }

    @ExceptionHandler({UserNotFoundException.class, FilmNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundException(RuntimeException e) {
        return "{\n  \"error\": \"" + e.getMessage() + "\"\n}";
    }
}
