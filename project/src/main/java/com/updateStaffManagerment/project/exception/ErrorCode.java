package com.updateStaffManagerment.project.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    DEPARTMENT_EXISTTED(1001,"Department already exists"),
    DEPARTMENT_NOT_FOUND(1002, "Department not found"),
    STAFF_EXISTED(1003,"Staff email already exists"),
    STAFF_NOT_FOUND(1004,"Staff not found"),
    USER_NOT_FOUND(1005,"User not found"),
    UNAUTHENTICATED(1006,"Unauthenticated"),
    UNAUTHORIZED(1007, "You do not have permission"), // Lỗi không đủ quyền (Bài 9 - Authorization)
    INVALID_TOKEN(1008, "Invalid token"), // Lỗi JWT không hợp lệ (Bài 7 - JWT)
    TOKEN_EXPIRED(1009, "Token expired"), // Lỗi JWT hết hạn (Bài 17 - Refresh Token)
    VALIDATION_ERROR(1010, "Validation error"); // Lỗi validation (Bài 4, 14 - Validation)


    private final int code;
    private final String message;

    ErrorCode(int code, String message){
        this.code = code;
        this.message = message;
    }
}
