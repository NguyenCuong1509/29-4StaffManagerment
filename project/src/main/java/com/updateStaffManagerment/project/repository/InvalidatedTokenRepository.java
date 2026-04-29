package com.updateStaffManagerment.project.repository;

import com.updateStaffManagerment.project.entity.InvalidatedToken; // Entity blacklist token (Bài 16 - Logout JWT)
import org.springframework.data.jpa.repository.JpaRepository; // CRUD repository (Bài 3 - Repository)

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> { // Repository cho blacklist token (Bài 16 - Logout JWT)
}