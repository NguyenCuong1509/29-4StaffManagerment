package com.updateStaffManagerment.project.service;

import com.updateStaffManagerment.project.dto.response.LoginResponse; // Response trả token mới (Bài 17 - Refresh Token)
import com.updateStaffManagerment.project.entity.RefreshToken; // Entity refresh token (Bài 17 - Refresh Token)
import com.updateStaffManagerment.project.entity.User; // User entity (Bài 6 - Authentication)
import com.updateStaffManagerment.project.exception.AppException; // Exception nghiệp vụ (Bài 15 - Exception)
import com.updateStaffManagerment.project.exception.ErrorCode; // ErrorCode token (Bài 15 - Exception)
import com.updateStaffManagerment.project.repository.RefreshTokenRepository; // Repository refresh token (Bài 17 - Refresh Token)
import com.updateStaffManagerment.project.security.JwtService; // Tạo access token mới (Bài 7 - JWT)
import lombok.RequiredArgsConstructor; // Constructor injection (Bài 5 - Lombok)
import org.springframework.beans.factory.annotation.Value; // Inject config từ application.yml (Bài 8 - @Value)
import org.springframework.stereotype.Service; // Service bean (Bài 4 - Service)
import org.springframework.transaction.annotation.Transactional; // Transaction DB (Bài 4 - Service)

import java.time.Instant; // Xử lý thời gian hết hạn (Bài 17 - Refresh Token)
import java.util.UUID; // Sinh refresh token random (Bài 17 - Refresh Token)

@Service // Đưa RefreshTokenService vào Spring container (Bài 17 - Refresh Token)
@RequiredArgsConstructor // Inject repository và jwtService (Bài 5 - Lombok)
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository; // Lưu và tìm refresh token (Bài 17 - Refresh Token)

    private final JwtService jwtService; // Tạo access token mới (Bài 7 - JWT)

    @Value("${jwt.refresh-token-expiration}") // Lấy thời gian sống refresh token từ application.yml (Bài 8 - @Value)
    private long refreshTokenExpiration; // Số giây refresh token sống (Bài 17 - Refresh Token)

    @Transactional // Lưu refresh token trong transaction (Bài 17 - Refresh Token)
    public String createRefreshToken(User user) { // Tạo refresh token cho user sau login (Bài 17 - Refresh Token)

        RefreshToken refreshToken = RefreshToken.builder() // Tạo refresh token entity (Bài 17 - Refresh Token)
                .token(UUID.randomUUID().toString()) // Sinh token ngẫu nhiên bằng UUID (Bài 17 - Refresh Token)
                .user(user) // Gắn token với user hiện tại (Bài 17 - Refresh Token)
                .expiryTime(Instant.now().plusSeconds(refreshTokenExpiration)) // Gán hạn token (Bài 17 - Refresh Token)
                .revoked(false) // Token mới chưa bị revoke (Bài 17 - Refresh Token)
                .build(); // Build entity (Bài 5 - Lombok)

        refreshTokenRepository.save(refreshToken); // Lưu refresh token xuống DB (Bài 17 - Refresh Token)

        return refreshToken.getToken(); // Trả token string cho client (Bài 17 - Refresh Token)
    }

    public LoginResponse refresh(String token) { // Cấp access token mới từ refresh token (Bài 17 - Refresh Token)

        RefreshToken refreshToken = refreshTokenRepository.findById(token) // Tìm refresh token trong DB (Bài 17 - Refresh Token)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_TOKEN)); // Không thấy thì token invalid (Bài 15 - Exception)

        if (refreshToken.isRevoked()) { // Check token đã bị revoke chưa (Bài 17 - Refresh Token)
            throw new AppException(ErrorCode.INVALID_TOKEN); // Revoked thì không cho dùng (Bài 15 - Exception)
        }

        if (refreshToken.getExpiryTime().isBefore(Instant.now())) { // Check token hết hạn chưa (Bài 17 - Refresh Token)
            throw new AppException(ErrorCode.TOKEN_EXPIRED); // Hết hạn thì báo lỗi (Bài 17 - Refresh Token)
        }

        User user = refreshToken.getUser(); // Lấy user sở hữu refresh token (Bài 17 - Refresh Token)

        String newAccessToken = jwtService.generateAccessToken(user); // Tạo access token mới (Bài 7 - JWT)

        return LoginResponse.builder() // Build response token mới (Bài 17 - Refresh Token)
                .accessToken(newAccessToken) // Trả access token mới (Bài 17 - Refresh Token)
                .refreshToken(refreshToken.getToken()) // Có thể trả lại refresh token cũ hoặc rotate token (Bài 17 - Refresh Token)
                .tokenType("Bearer") // Token type cho Authorization header (Bài 7 - JWT)
                .build(); // Build response (Bài 5 - Lombok)
    }
}