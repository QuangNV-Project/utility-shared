package com.quangnv.service.utility_shared.util;

import jakarta.validation.ValidationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

public class ValidationUtil {
    private ValidationUtil() {
    }

    public static List<String> getErrorMessages(BindingResult bindingResult) {
        if (bindingResult == null || !bindingResult.hasErrors()) {
            return List.of(); // trả về list rỗng nếu không có lỗi
        }

        return bindingResult.getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();
    }

    // Cũ vẫn giữ nguyên
    public static void checkResult(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> messages = getErrorMessages(bindingResult);
            throw new ValidationException(String.join(", ", messages));
        }
    }

    public static void checkNotNull(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }
    }

    public static void checkNotNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void checkNotEmpty(String str) {
        checkNotEmpty(str, "String argument cannot be empty");
    }

    public static void checkNotEmpty(String str, String message) {
        checkNotNull(str, message);
        if (str.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    public static <T> void checkNotEmpty(List<T> list) {
        checkNotEmpty(list, "List argument cannot be empty");
    }

    public static <T> void checkNotEmpty(List<T> list, String message) {
        checkNotNull(list, message);
        if (list.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }
}

