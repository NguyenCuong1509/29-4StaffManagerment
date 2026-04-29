package com.updateStaffManagerment.project.service;
import com.updateStaffManagerment.project.repository.InvalidatedTokenRepository; // Repository check blacklist (Bài 16 - Logout JWT)
import lombok.RequiredArgsConstructor; // Constructor injection (Bài 5 - Lombok)
import org.springframework.stereotype.Service; // Service bean (Bài 4 - Service)

@Service // Đưa service vào Spring container (Bài 16 - Logout JWT)
@RequiredArgsConstructor // Inject repository qua constructor (Bài 5 - Lombok)
public class InvalidatedTokenService {

    private final InvalidatedTokenRepository invalidatedTokenRepository; // Query table invalidated_tokens (Bài 16 - Logout JWT)

    public boolean isInvalidated(String jwtId) { // Kiểm tra token đã bị logout chưa (Bài 16 - Logout JWT)
        return invalidatedTokenRepository.existsById(jwtId); // Nếu jwtId tồn tại trong DB nghĩa là token bị blacklist (Bài 16 - Logout JWT)
    }
}