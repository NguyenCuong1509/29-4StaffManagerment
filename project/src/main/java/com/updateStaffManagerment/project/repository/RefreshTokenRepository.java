package com.updateStaffManagerment.project.repository;
import com.updateStaffManagerment.project.entity.RefreshToken; // Entity refresh token (Bài 17 - Refresh Token)
import org.springframework.data.jpa.repository.JpaRepository; // CRUD repository (Bài 3 - Repository)

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> { // Repository cho refresh_tokens (Bài 17 - Refresh Token)
}
