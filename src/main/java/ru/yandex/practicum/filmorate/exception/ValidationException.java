package ru.yandex.practicum.filmorate.exception;

public class ValidationException extends IllegalArgumentException {
    public final String fieldName;

    public ValidationException(String s, String fieldName) {
        super(s);
        this.fieldName = fieldName;
    }

    public ValidationException(String message, String fieldName, Throwable cause) {
        super(message, cause);
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
