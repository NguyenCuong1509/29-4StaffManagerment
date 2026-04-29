package com.updateStaffManagerment.project.service;

import com.updateStaffManagerment.project.entity.InvalidatedToken; // Entity blacklist token (Bài 16 - Logout JWT)
import com.updateStaffManagerment.project.exception.AppException; // Exception nghiệp vụ (Bài 15 - Exception)
import com.updateStaffManagerment.project.exception.ErrorCode; // ErrorCode token (Bài 15 - Exception)
import com.updateStaffManagerment.project.repository.InvalidatedTokenRepository; // Repository lưu blacklist (Bài 16 - Logout JWT)
import com.nimbusds.jwt.SignedJWT; // Parse JWT lấy claims (Bài 7 - JWT)
import jakarta.servlet.http.HttpServletRequest; // Đọc Authorization header (Bài 16 - Logout JWT)
import lombok.RequiredArgsConstructor; // Constructor injection (Bài 5 - Lombok)
import org.springframework.stereotype.Service; // Service bean (Bài 4 - Service)

import java.text.ParseException; // Lỗi parse JWT (Bài 7 - JWT)
import java.time.Instant; // Thời gian hết hạn (Bài 16 - Logout JWT)

@Service // Đưa LogoutService vào Spring container (Bài 16 - Logout JWT)
@RequiredArgsConstructor // Inject repository qua constructor (Bài 5 - Lombok)
public class LogoutService {

    private final InvalidatedTokenRepository invalidatedTokenRepository; // Lưu token bị blacklist (Bài 16 - Logout JWT)

    public void logout(HttpServletRequest request) { // Xử lý logout từ request (Bài 16 - Logout JWT)

        String authHeader = request.getHeader("Authorization"); // Lấy header Authorization (Bài 16 - Logout JWT)

        if (authHeader == null || !authHeader.startsWith("Bearer ")) { // Kiểm tra có Bearer token không (Bài 16 - Logout JWT)
            throw new AppException(ErrorCode.INVALID_TOKEN); // Không có token thì báo lỗi (Bài 15 - Exception)
        }

        String token = authHeader.substring(7); // Cắt prefix Bearer để lấy JWT (Bài 16 - Logout JWT)

        try { // Bắt lỗi parse token (Bài 7 - JWT)
            SignedJWT signedJWT = SignedJWT.parse(token); // Parse JWT để lấy jwtId và expiry (Bài 7 - JWT)

            String jwtId = signedJWT.getJWTClaimsSet().getJWTID(); // Lấy claim jti làm ID blacklist (Bài 16 - Logout JWT)

            Instant expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime().toInstant(); // Lấy hạn token để lưu (Bài 16 - Logout JWT)

            InvalidatedToken invalidatedToken = InvalidatedToken.builder() // Tạo entity blacklist token (Bài 16 - Logout JWT)
                    .id(jwtId) // Gán jwtId làm khóa chính (Bài 16 - Logout JWT)
                    .expiryTime(expiryTime) // Gán thời điểm hết hạn (Bài 16 - Logout JWT)
                    .build(); // Build entity (Bài 5 - Lombok)

            invalidatedTokenRepository.save(invalidatedToken); // Lưu token vào blacklist DB (Bài 16 - Logout JWT)

        } catch (ParseException e) { // Bắt lỗi parse token (Bài 7 - JWT)
            throw new AppException(ErrorCode.INVALID_TOKEN); // Token sai format thì báo lỗi (Bài 15 - Exception)
        }
    }
}