package com.quangnv.service.utility_shared.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

public class JsonConvertException extends JsonProcessingException {
    public JsonConvertException(String message) {
        super(message);
    }

    public JsonConvertException(String message, Throwable cause) {
        super(message, cause);
    }
}
