package com.updateStaffManagerment.project.entity;
import jakarta.persistence.*; // JPA annotation (Bài 17 - Refresh Token)
import lombok.*; // Lombok (Bài 5 - Lombok)

import java.time.Instant; // Thời gian hết hạn token (Bài 17 - Refresh Token)

@Getter // Getter (Bài 5 - Lombok)
@Setter // Setter (Bài 5 - Lombok)
@NoArgsConstructor // Constructor rỗng cho JPA (Bài 2 - Entity)
@AllArgsConstructor // Constructor đầy đủ (Bài 5 - Lombok)
@Builder // Builder pattern (Bài 5 - Lombok)
@Entity // Mapping thành table refresh_tokens (Bài 17 - Refresh Token)
@Table(name = "refresh_tokens") // Tên table refresh token (Bài 17 - Refresh Token)
public class RefreshToken {

    @Id // Token string làm khóa chính (Bài 17 - Refresh Token)
    @Column(length = 100) // Giới hạn độ dài token (Bài 17 - Refresh Token)
    private String token; // Refresh token dạng random UUID (Bài 17 - Refresh Token)

    @ManyToOne(fetch = FetchType.LAZY) // Nhiều refresh token có thể thuộc một user (Bài 17 - Refresh Token)
    @JoinColumn(name = "user_id", nullable = false) // FK trỏ tới users.id (Bài 17 - Refresh Token)
    private User user; // User sở hữu refresh token (Bài 17 - Refresh Token)

    private Instant expiryTime; // Thời điểm refresh token hết hạn (Bài 17 - Refresh Token)

    private boolean revoked; // Đánh dấu token đã bị thu hồi chưa (Bài 17 - Refresh Token)
}