package com.quangnv.service.utility_shared.util;

import jakarta.validation.ValidationException;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidationUtil {
  private ValidationUtil() {
  }

  public static void checkResult(BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      Map<String, String> errors = new HashMap<>();
      bindingResult.getFieldErrors().forEach((error) -> errors.put(error.getField(), error.getDefaultMessage()));
      StringBuilder errorMsg = new StringBuilder();

      for (Map.Entry<String, String> entry : errors.entrySet()) {
        String var10001 = entry.getKey();
        errorMsg.append("Error: ").append(var10001).append(" - ")
          .append(entry.getValue());
        errorMsg.append("\n");
      }

      throw new ValidationException(errorMsg.toString());
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

