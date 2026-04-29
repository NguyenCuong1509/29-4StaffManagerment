package com.updateStaffManagerment.project.config;


import lombok.RequiredArgsConstructor; // Constructor injection (Bài 5 - Lombok)
import org.springframework.context.annotation.Bean; // Khai báo Bean (Bài 8 - Config)
import org.springframework.context.annotation.Configuration; // Class config (Bài 8 - Config)
import org.springframework.security.authentication.AuthenticationManager; // Manager xác thực (Bài 6 - Authentication)
import org.springframework.security.authentication.AuthenticationProvider; // Provider xử lý authentication (Bài 6 - Authentication)
import org.springframework.security.authentication.dao.DaoAuthenticationProvider; // Provider dùng UserDetailsService + PasswordEncoder (Bài 6 - Authentication)
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; // Lấy AuthenticationManager từ Spring (Bài 8 - Config)
import org.springframework.security.core.userdetails.UserDetailsService; // Load user từ DB (Bài 6 - UserDetailsService)
import org.springframework.security.crypto.password.PasswordEncoder; // BCrypt encoder (Bài 6 - BCrypt)

@Configuration // Khai báo class cấu hình security bean (Bài 8 - Config)
@RequiredArgsConstructor // Inject dependencies qua constructor (Bài 5 - Lombok)
public class AuthenticationConfig {

    private final UserDetailsService userDetailsService; // Service load user từ DB (Bài 6 - UserDetailsService)

    private final PasswordEncoder passwordEncoder; // Bean BCrypt dùng so khớp password (Bài 6 - BCrypt)

    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
    @Bean // Đưa AuthenticationManager vào Spring container (Bài 8 - Security Config)
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception { // Lấy manager từ config của Spring (Bài 8 - Config)
        return configuration.getAuthenticationManager(); // Trả AuthenticationManager để AuthService inject (Bài 6 - Authentication)
    }
}


