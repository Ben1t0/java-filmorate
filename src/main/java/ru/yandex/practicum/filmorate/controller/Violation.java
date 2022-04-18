package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class Violation {
    private final String fieldName;
    private final String message;
}
