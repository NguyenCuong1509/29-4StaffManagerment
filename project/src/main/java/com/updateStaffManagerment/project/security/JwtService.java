package com.updateStaffManagerment.project.security;

import com.updateStaffManagerment.project.entity.Permission; // Entity permission để đưa vào token (Bài 13 - Claims)
import com.updateStaffManagerment.project.entity.Role; // Entity role để lấy permission (Bài 12 - Role Permission)
import com.updateStaffManagerment.project.entity.User; // User dùng để tạo token (Bài 7 - JWT)
import com.updateStaffManagerment.project.exception.AppException; // Exception nghiệp vụ (Bài 15 - Exception)
import com.updateStaffManagerment.project.exception.ErrorCode; // ErrorCode JWT (Bài 15 - Exception)
import com.nimbusds.jose.*; // JWS header, signer, verifier (Bài 7 - JWT)
import com.nimbusds.jose.crypto.MACSigner; // Ký JWT bằng HMAC secret (Bài 7 - JWT)
import com.nimbusds.jose.crypto.MACVerifier; // Verify JWT bằng HMAC secret (Bài 7 - JWT)
import com.nimbusds.jwt.JWTClaimsSet; // Payload claims của JWT (Bài 7, 13 - JWT Claims)
import com.nimbusds.jwt.SignedJWT; // JWT đã ký (Bài 7 - JWT)
import org.springframework.beans.factory.annotation.Value; // Inject giá trị từ application.yml (Bài 8 - @Value)
import org.springframework.stereotype.Service; // Đưa JwtService vào Spring container (Bài 7 - JWT)

import java.text.ParseException; // Lỗi parse JWT (Bài 7 - JWT)
import java.time.Instant; // Xử lý thời gian hiện tại (Bài 17 - Token Expiration)
import java.util.Date; // Date cho JWT claims (Bài 7 - JWT)
import java.util.Set; // Set authority (Bài 13 - Claims)
import java.util.UUID; // Tạo JWT ID để blacklist logout (Bài 16 - Logout JWT)
import java.util.stream.Collectors; // Build scope string (Bài 13 - Claims)

@Service // JwtService là Bean xử lý token (Bài 7 - JWT)
public class JwtService {

    @Value("${jwt.secret}") // Lấy secret từ application.yml thay vì hard-code (Bài 8 - @Value)
    private String secret; // Secret dùng để ký và verify JWT (Bài 7 - JWT)

    @Value("${jwt.access-token-expiration}") // Lấy thời gian sống access token từ config (Bài 17 - Refresh Token)
    private long accessTokenExpiration; // Số giây access token sống (Bài 17 - Refresh Token)

    public String generateAccessToken(User user) { // Tạo access token sau khi login (Bài 7 - JWT)
        try { // Bắt lỗi ký token (Bài 7 - JWT)
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS256); // Header khai báo thuật toán HS256 (Bài 7 - JWT)

            String scope = buildScope(user); // Lấy roles và permissions đưa vào token (Bài 13 - Gắn quyền vào token)

            JWTClaimsSet claims = new JWTClaimsSet.Builder() // Tạo payload JWT (Bài 7 - JWT)
                    .subject(user.getUsername()) // subject là username của user (Bài 7 - JWT)
                    .issuer("security-demo") // issuer cho biết token do app nào phát hành (Bài 7 - JWT)
                    .issueTime(new Date()) // Thời điểm phát hành token (Bài 7 - JWT)
                    .expirationTime(Date.from(Instant.now().plusSeconds(accessTokenExpiration))) // Thời điểm hết hạn (Bài 17 - Refresh Token)
                    .jwtID(UUID.randomUUID().toString()) // ID token dùng cho blacklist logout (Bài 16 - Logout JWT)
                    .claim("scope", scope) // Gắn quyền vào token để authorization (Bài 13 - Claims)
                    .build(); // Build claims (Bài 7 - JWT)

            SignedJWT signedJWT = new SignedJWT(header, claims); // Ghép header và claims thành JWT (Bài 7 - JWT)

            signedJWT.sign(new MACSigner(secret.getBytes())); // Ký JWT bằng secret key (Bài 7 - JWT)

            return signedJWT.serialize(); // Convert JWT thành chuỗi trả cho client (Bài 7 - JWT)
        } catch (JOSEException e) { // Bắt lỗi ký JWT (Bài 7 - JWT)
            throw new AppException(ErrorCode.INVALID_TOKEN); // Ném lỗi token (Bài 15 - Exception)
        }
    }

    public SignedJWT verifyToken(String token) { // Verify access token từ client (Bài 7 - JWT)
        try { // Bắt lỗi parse và verify (Bài 7 - JWT)
            SignedJWT signedJWT = SignedJWT.parse(token); // Parse chuỗi token thành SignedJWT (Bài 7 - JWT)

            boolean verified = signedJWT.verify(new MACVerifier(secret.getBytes())); // Kiểm tra chữ ký token (Bài 7 - JWT)

            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime(); // Lấy thời điểm hết hạn (Bài 17 - Refresh Token)

            if (!verified || expirationTime.before(new Date())) { // Token sai chữ ký hoặc hết hạn (Bài 7, 17)
                throw new AppException(ErrorCode.INVALID_TOKEN); // Ném lỗi token không hợp lệ (Bài 15 - Exception)
            }

            return signedJWT; // Trả token đã verify cho filter dùng tiếp (Bài 7 - JWT)
        } catch (ParseException | JOSEException e) { // Bắt lỗi parse hoặc verify (Bài 7 - JWT)
            throw new AppException(ErrorCode.INVALID_TOKEN); // Ném lỗi token không hợp lệ (Bài 15 - Exception)
        }
    }

    public String getUsername(String token) { // Lấy username từ token (Bài 7 - JWT)
        try { // Bắt lỗi parse token (Bài 7 - JWT)
            return SignedJWT.parse(token).getJWTClaimsSet().getSubject(); // subject chính là username (Bài 7 - JWT)
        } catch (ParseException e) { // Bắt lỗi parse (Bài 7 - JWT)
            throw new AppException(ErrorCode.INVALID_TOKEN); // Ném lỗi token không hợp lệ (Bài 15 - Exception)
        }
    }

    private String buildScope(User user) { // Build quyền đưa vào claim scope (Bài 13 - Claims)
        Set<String> authorities = user.getRoles().stream() // Duyệt danh sách role của user (Bài 12 - Role Permission)
                .flatMap(role -> role.getPermissions().stream()) // Lấy toàn bộ permission từ role (Bài 12 - ManyToMany)
                .map(Permission::getName) // Lấy tên permission như STAFF_CREATE (Bài 13 - Claims)
                .collect(Collectors.toSet()); // Gom thành Set để tránh trùng (Bài 13 - Claims)

        user.getRoles().stream() // Duyệt role lần nữa (Bài 12 - Role Permission)
                .map(Role::getName) // Lấy tên role như ADMIN (Bài 12 - Role Permission
                .collect(Collectors.toSet()); // Gom permission thành Set để tránh trùng quyền (Bài 13 - Claims)

        user.getRoles().stream() // Duyệt lại danh sách role của user (Bài 12 - Role Permission)
                .map(Role::getName) // Lấy tên role như ADMIN, HR, USER (Bài 12 - Role Permission)
                .map(role -> "ROLE_" + role) // Thêm prefix ROLE_ theo convention Spring Security (Bài 9 - Authorization)
                .forEach(authorities::add); // Thêm role vào tập authority chung (Bài 13 - Claims)

        return String.join(" ", authorities); // Ghép quyền thành chuỗi scope cách nhau bằng dấu cách (Bài 13 - Claims)
    }
}