package com.updateStaffManagerment.project.exception;

import com.updateStaffManagerment.project.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandle {
    @ExceptionHandler(AppException.class) // Bắt lỗi nghiệp vụ tự định nghĩa (Bài 15 - Global Exception nâng cao)
    public ResponseEntity<ApiResponse<Void>> handleAppException(AppException ex) { // Xử lý AppException (Bài 15 - Global Exception nâng cao)
        ErrorCode errorCode = ex.getErrorCode(); // Lấy ErrorCode từ exception (Bài 15 - Global Exception nâng cao)

        return ResponseEntity.badRequest().body( // Trả HTTP 400 cho lỗi nghiệp vụ đơn giản (Bài 15 - Global Exception nâng cao)
                ApiResponse.<Void>builder() // Build response lỗi (Bài 15 - Global Exception nâng cao)
                        .code(errorCode.getCode()) // Gán mã lỗi (Bài 15 - Global Exception nâng cao)
                        .message(errorCode.getMessage()) // Gán message lỗi (Bài 15 - Global Exception nâng cao)
                        .build() // Tạo response object (Bài 15 - Global Exception nâng cao)
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex){
        String message = ex.getBindingResult().getFieldError().getDefaultMessage();

        return ResponseEntity.badRequest().body(
                ApiResponse.<Void>builder()
                        .code(ErrorCode.VALIDATION_ERROR.getCode())
                        .message(message)
                        .build()
        );
    }
}
