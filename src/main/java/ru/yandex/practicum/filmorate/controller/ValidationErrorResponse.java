package ru.yandex.practicum.filmorate.controller;

import lombok.Data;

import java.util.List;

@Data
public class ValidationErrorResponse {
    private final List<Violation> violations;
}
