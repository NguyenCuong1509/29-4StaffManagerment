package com.updateStaffManagerment.project.security;
import com.nimbusds.jwt.SignedJWT; // JWT đã verify (Bài 7 - JWT)
import com.updateStaffManagerment.project.service.InvalidatedTokenService;
import jakarta.servlet.FilterChain; // Chuỗi filter tiếp theo (Bài 8 - Filter Chain)
import jakarta.servlet.ServletException; // Exception servlet (Bài 8 - Filter Chain)
import jakarta.servlet.http.HttpServletRequest; // HTTP request (Bài 8 - Filter Chain)
import jakarta.servlet.http.HttpServletResponse; // HTTP response (Bài 8 - Filter Chain)
import lombok.RequiredArgsConstructor; // Constructor injection (Bài 5 - Lombok)
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Authentication object cho SecurityContext (Bài 9 - Authorization)
import org.springframework.security.core.authority.SimpleGrantedAuthority; // Authority của user (Bài 9 - Authorization)
import org.springframework.security.core.context.SecurityContextHolder; // Nơi lưu Authentication hiện tại (Bài 9 - Authorization)
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource; // Gắn chi tiết request vào auth (Bài 8 - Filter)
import org.springframework.stereotype.Component; // Đưa filter vào Spring container (Bài 8 - Filter)
import org.springframework.web.filter.OncePerRequestFilter; // Filter chạy một lần mỗi request (Bài 8 - Filter)

import java.io.IOException; // Exception IO (Bài 8 - Filter)
import java.text.ParseException; // Lỗi parse claims (Bài 7 - JWT)
import java.util.Arrays; // Tách scope thành array (Bài 13 - Claims)
import java.util.List; // List authority (Bài 9 - Authorization)
import java.util.stream.Collectors; // Convert scope sang authority (Bài 13 - Claims)

@Component // Filter là Spring Bean để inject vào SecurityConfig (Bài 8 - Security Config)
@RequiredArgsConstructor // Inject JwtService và blacklist service (Bài 5 - Lombok)
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService; // Service verify JWT (Bài 7 - JWT)

    private final InvalidatedTokenService invalidatedTokenService; // Check token đã logout chưa (Bài 16 - Logout JWT)

    @Override // Override filter logic (Bài 8 - Filter Chain)
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) // Method chạy trước controller (Bài 8 - Filter)
            throws ServletException, IOException { // Khai báo exception servlet và IO (Bài 8 - Filter)

        String authHeader = request.getHeader("Authorization"); // Lấy header Authorization từ request (Bài 7 - JWT)

        if (authHeader == null || !authHeader.startsWith("Bearer ")) { // Nếu không có Bearer token (Bài 7 - JWT)
            filterChain.doFilter(request, response); // Cho request đi tiếp, Spring Security sẽ tự quyết định chặn hay không (Bài 8 - Filter Chain)
            return; // Kết thúc filter hiện tại (Bài 8 - Filter Chain)
        }

        String token = authHeader.substring(7); // Cắt "Bearer " để lấy token thật (Bài 7 - JWT)

        try { // Bắt lỗi verify token (Bài 7 - JWT)
            SignedJWT signedJWT = jwtService.verifyToken(token); // Verify chữ ký và hạn token (Bài 7 - JWT)

            String jwtId = signedJWT.getJWTClaimsSet().getJWTID(); // Lấy JWT ID để check blacklist (Bài 16 - Logout JWT)

            if (invalidatedTokenService.isInvalidated(jwtId)) { // Kiểm tra token đã logout chưa (Bài 16 - Logout JWT)
                filterChain.doFilter(request, response); // Không set Authentication, request coi như chưa đăng nhập (Bài 16 - Logout JWT)
                return; // Dừng xử lý filter hiện tại (Bài 16 - Logout JWT)
            }

            String username = signedJWT.getJWTClaimsSet().getSubject(); // Lấy username từ subject (Bài 7 - JWT)

            String scope = signedJWT.getJWTClaimsSet().getStringClaim("scope"); // Lấy quyền từ claim scope (Bài 13 - Claims)

            List<SimpleGrantedAuthority> authorities = Arrays.stream(scope.split(" ")) // Tách chuỗi scope thành từng quyền (Bài 13 - Claims)
                    .map(SimpleGrantedAuthority::new) // Convert quyền thành GrantedAuthority (Bài 9 - Authorization)
                    .collect(Collectors.toList()); // Gom thành list authority (Bài 9 - Authorization)

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken( // Tạo Authentication cho Spring Security (Bài 9 - Authorization)
                    username, // Principal là username (Bài 9 - Authorization)
                    null, // Không lưu credentials sau khi xác thực JWT (Bài 9 - Authorization)
                    authorities // Danh sách quyền lấy từ token (Bài 13 - Claims)
            );

            authentication.setDetails( // Gắn thêm thông tin request (Bài 8 - Filter)
                    new WebAuthenticationDetailsSource().buildDetails(request) // Build details từ request hiện tại (Bài 8 - Filter)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication); // Đưa Authentication vào SecurityContext (Bài 9 - Authorization)

        } catch (Exception e) { // Nếu token lỗi, hết hạn, parse lỗi (Bài 7 - JWT)
            SecurityContextHolder.clearContext(); // Xóa context để tránh request được xem là authenticated (Bài 9 - Authorization)
        }

        filterChain.doFilter(request, response); // Chuyển request tới filter tiếp theo hoặc controller (Bài 8 - Filter Chain)
    }
}