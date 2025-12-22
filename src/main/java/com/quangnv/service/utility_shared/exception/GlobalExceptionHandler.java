package com.quangnv.service.utility_shared.exception;

import com.quangnv.service.utility_shared.dto.ApiResponse;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Helper để tạo response nhanh và thống nhất
    private ResponseEntity<ApiResponse<Object>> buildResponse(String message, Object errors, HttpStatus status) {
        ApiResponse<Object> response = ApiResponse.error(message, errors, status.value());
        return ResponseEntity.status(status).body(response);
    }

    // 1. Xử lý Validation (@Valid, @NotBlank, v.v.)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        log.error("Validations failed: {}", errors);
        return buildResponse("Dữ liệu không hợp lệ", errors, HttpStatus.BAD_REQUEST);
    }

    // 1. Xử lý Validation Spring mặc định.)
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(ValidationException ex) {
        log.error("Validation failed: {}", ex.getMessage());
        return buildResponse(ex.getMessage(), null, HttpStatus.BAD_REQUEST);
    }

    // 2. Xử lý Không tìm thấy Resource (404)
    @ExceptionHandler({ResourceNotFoundException.class, UserNotFoundException.class, NotFoundException.class})
    public ResponseEntity<ApiResponse<Object>> handleNotFoundException(RuntimeException ex) {
        log.error("Resource not found: {}", ex.getMessage());
        return buildResponse(ex.getMessage(), null, HttpStatus.NOT_FOUND);
    }

    // 3. Xử lý Lỗi nghiệp vụ (Business Logic - 400)
    @ExceptionHandler({BusinessException.class, CustomException.class})
    public ResponseEntity<ApiResponse<Object>> handleBusinessException(RuntimeException ex) {
        log.error("Business error: {}", ex.getMessage());
        return buildResponse(ex.getMessage(), null, HttpStatus.BAD_REQUEST);
    }

    // 4. Xử lý Xác thực (401 Unauthorized) - Quan trọng để Axios kích hoạt Refresh Token
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Object>> handleUnauthorizedException(UnauthorizedException ex) {
        log.error("Unauthorized: {}", ex.getMessage());
        return buildResponse(ex.getMessage(), "Token invalid or expired", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(
                        "You do not have permission to access this resource",
                        "ACCESS_DENIED",
                        HttpStatus.FORBIDDEN.value()
                ));
    }


    // 6. Xử lý Lỗi HTTP chung (Dùng cho HttpException của bạn)
    @ExceptionHandler(HttpException.class)
    public ResponseEntity<ApiResponse<Object>> handleHttpException(HttpException ex) {
        log.error("HTTP error: {}", ex.getMessage());
        // Giả sử HttpException của bạn có chứa HttpStatus bên trong
        return buildResponse(ex.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 7. Xử lý tất cả các lỗi không xác định (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
        log.error("Unexpected system error: ", ex); // Log full stack trace cho lỗi 500
        return buildResponse("Lỗi hệ thống", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}