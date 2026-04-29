package com.updateStaffManagerment.project.controller;

import com.updateStaffManagerment.project.dto.request.LoginRequest; // DTO login (Bài 6 - Authentication)
import com.updateStaffManagerment.project.dto.request.RegisterRequest; // DTO register (Bài 6 - Authentication)
import com.updateStaffManagerment.project.dto.response.ApiResponse; // Response chuẩn (Bài 15 - ApiResponse)
import com.updateStaffManagerment.project.dto.response.LoginResponse; // Response token (Bài 7 - JWT)
import com.updateStaffManagerment.project.service.AuthService; // Service auth (Bài 6 - Authentication)
import com.updateStaffManagerment.project.service.LogoutService; // Service logout JWT blacklist (Bài 16 - Logout JWT)
import com.updateStaffManagerment.project.service.RefreshTokenService; // Service refresh token (Bài 17 - Refresh Token)
import jakarta.servlet.http.HttpServletRequest; // Đọc Authorization header khi logout (Bài 16 - Logout JWT)
import jakarta.validation.Valid; // Kích hoạt validation request (Bài 4 - Validation)
import lombok.RequiredArgsConstructor; // Constructor injection (Bài 5 - Lombok)
import org.springframework.web.bind.annotation.*; // REST annotation (Bài 5 - Controller)

@RestController // REST controller trả JSON (Bài 5 - Controller)
@RequestMapping("/auth") // Prefix /auth cho API xác thực (Bài 6 - Authentication)
@RequiredArgsConstructor // Inject service bằng constructor (Bài 5 - Lombok)
public class AuthController {

    private final AuthService authService; // Xử lý register/login (Bài 6 - Authentication)

    private final LogoutService logoutService; // Xử lý logout bằng blacklist JWT (Bài 16 - Logout JWT)

    private final RefreshTokenService refreshTokenService; // Xử lý cấp lại access token (Bài 17 - Refresh Token)

    @PostMapping("/register") // Handle POST /auth/register (Bài 6 - Authentication)
    public ApiResponse<Void> register(@RequestBody @Valid RegisterRequest request) { // Nhận request và validate (Bài 4, 6)
        authService.register(request); // Gọi service tạo user mới (Bài 6 - Authentication)
        return ApiResponse.success(null); // Trả response thành công chuẩn hóa (Bài 15 - ApiResponse)
    }

    @PostMapping("/login") // Handle POST /auth/login (Bài 6 - Authentication)
    public ApiResponse<LoginResponse> login(@RequestBody @Valid LoginRequest request) { // Nhận username/password (Bài 6 - Authentication)
        return ApiResponse.success(authService.login(request)); // Xác thực và trả JWT (Bài 7 - JWT)
    }

    @PostMapping("/logout") // Handle POST /auth/logout (Bài 16 - Logout JWT)
    public ApiResponse<Void> logout(HttpServletRequest request) { // Lấy request để đọc token (Bài 16 - Logout JWT)
        logoutService.logout(request); // Đưa JWT vào blacklist (Bài 16 - Logout JWT)
        return ApiResponse.success(null); // Trả response thành công (Bài 15 - ApiResponse)
    }

    @PostMapping("/refresh") // Handle POST /auth/refresh (Bài 17 - Refresh Token)
    public ApiResponse<LoginResponse> refresh(@RequestParam String refreshToken) { // Nhận refresh token từ client (Bài 17 - Refresh Token)
        return ApiResponse.success(refreshTokenService.refresh(refreshToken)); // Verify refresh token và cấp token mới (Bài 17 - Refresh Token)
    }
}