package com.updateStaffManagerment.project.config;

import com.updateStaffManagerment.project.security.JwtAuthenticationFilter; // Filter xử lý JWT (Bài 8 - Filter)
import lombok.RequiredArgsConstructor; // Constructor injection (Bài 5 - Lombok)
import org.springframework.context.annotation.Bean; // Khai báo Bean (Bài 8 - Config)
import org.springframework.context.annotation.Configuration; // Class config (Bài 8 - Config)
import org.springframework.security.authentication.AuthenticationProvider; // Provider xác thực DB (Bài 6 - Authentication)
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // Bật @PreAuthorize, @PostAuthorize (Bài 10, 11)
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // DSL cấu hình security HTTP (Bài 8 - Security Config)
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // Bật web security (Bài 8 - Security Config)
import org.springframework.security.config.http.SessionCreationPolicy; // Cấu hình stateless session (Bài 7 - JWT Stateless)
import org.springframework.security.web.SecurityFilterChain; // Bean filter chain chính (Bài 8 - Security Config)
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // Filter mặc định username/password (Bài 8 - Filter Chain)

@Configuration // Class chứa cấu hình Spring Security (Bài 8 - Security Config)
@EnableWebSecurity // Bật Spring Security cho web app (Bài 8 - Security Config)
@EnableMethodSecurity // Bật @PreAuthorize và @PostAuthorize (Bài 10, 11 - Method Security)
@RequiredArgsConstructor // Inject dependencies qua constructor (Bài 5 - Lombok)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter; // Filter đọc và verify JWT (Bài 8 - JWT Filter)

    private final AuthenticationProvider authenticationProvider; // Provider dùng UserDetailsService + BCrypt (Bài 6 - Authentication)

    @Bean // Đưa SecurityFilterChain vào Spring container (Bài 8 - Security Config)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { // Cấu hình bảo mật HTTP (Bài 8 - Security Config)

        http.csrf(csrf -> csrf.disable()); // Tắt CSRF vì API dùng JWT stateless qua Authorization header (Bài 8 - Security Config)

        http.sessionManagement(session -> session // Cấu hình session management (Bài 7 - JWT Stateless)
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Không tạo session phía server, mỗi request tự mang JWT (Bài 7 - Stateless)

        http.authenticationProvider(authenticationProvider); // Gắn provider xác thực username/password từ DB (Bài 6 - Authentication)

        http.authorizeHttpRequests(auth -> auth // Cấu hình rule phân quyền URL (Bài 8 - Security Config)
                .requestMatchers("/auth/login", "/auth/register", "/auth/refresh").permitAll() // Cho login/register/refresh không cần token (Bài 8 - Security Config)
                .anyRequest().authenticated() // Các API còn lại bắt buộc authenticated (Bài 8 - Security Config)
        );

        http.addFilterBefore( // Thêm custom JWT filter vào filter chain (Bài 8 - JWT Filter)
                jwtAuthenticationFilter, // Filter tự viết để đọc token (Bài 8 - JWT Filter)
                UsernamePasswordAuthenticationFilter.class // Đặt trước filter username/password mặc định (Bài 8 - Filter Chain)
        );

        return http.build(); // Build SecurityFilterChain cho Spring Security dùng (Bài 8 - Security Config)
    }
}