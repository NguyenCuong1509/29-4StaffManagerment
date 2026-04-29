package com.updateStaffManagerment.project.dto.request;

import com.updateStaffManagerment.project.validation.NoWhitespace;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 4,max = 100,message = "Username must be between 4 and 100 character")
    @NoWhitespace(message = "Username must not contain whitespace") // Không cho username chứa khoảng trắng (Bài 14 - Custom Validation)
    String username; // Username đăng ký (Bài 6 - Authentication)
    @NotBlank(message = "Username is required")
    @Size(min = 8 , message = "Password must be at least 8 character")
    String password;
    Set<String> roles;
}
