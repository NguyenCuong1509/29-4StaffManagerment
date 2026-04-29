package com.updateStaffManagerment.project.entity;

import jakarta.persistence.*; // JPA annotation (Bài 16 - Logout JWT)
        import lombok.*; // Lombok (Bài 5 - Lombok)

        import java.time.Instant; // Thời gian hết hạn token (Bài 16 - Logout JWT)

@Getter // Getter (Bài 5 - Lombok)
@Setter // Setter (Bài 5 - Lombok)
@NoArgsConstructor // Constructor rỗng cho JPA (Bài 2 - Entity)
@AllArgsConstructor // Constructor đầy đủ (Bài 5 - Lombok)
@Builder // Builder pattern (Bài 5 - Lombok)
@Entity // Mapping thành table invalidated_tokens (Bài 16 - Logout JWT)
@Table(name = "invalidated_tokens") // Tên table lưu token blacklist (Bài 16 - Logout JWT)
public class InvalidatedToken {

    @Id // jwtId là khóa chính vì mỗi token có một ID riêng (Bài 16 - Logout JWT)
    @Column(length = 100) // Giới hạn độ dài jwtId (Bài 16 - Logout JWT)
    private String id; // JWT ID lấy từ claim jti (Bài 16 - Logout JWT)

    private Instant expiryTime; // Thời điểm token hết hạn để sau này cleanup (Bài 16 - Logout JWT)
}