package com.updateStaffManagerment.project.controller;

import com.updateStaffManagerment.project.dto.request.StaffCreateRequest; // Request tạo staff (Bài 5 - DTO)
import com.updateStaffManagerment.project.dto.response.ApiResponse; // Response chuẩn (Bài 15 - ApiResponse)
import com.updateStaffManagerment.project.dto.response.StaffResponse; // Staff response DTO (Bài 5 - DTO)
import com.updateStaffManagerment.project.service.StaffService; // Service staff (Bài 4 - Service)
import jakarta.validation.Valid; // Kích hoạt validation (Bài 4 - Validation)
import lombok.RequiredArgsConstructor; // Constructor injection (Bài 5 - Lombok)
import org.springframework.security.access.prepost.PostAuthorize; // Kiểm tra quyền sau khi method chạy (Bài 11 - @PostAuthorize)
import org.springframework.security.access.prepost.PreAuthorize; // Kiểm tra quyền trước khi method chạy (Bài 10 - @PreAuthorize)
import org.springframework.web.bind.annotation.*; // REST annotation (Bài 5 - Controller)

import java.util.List; // List response (Bài 4 - CRUD)

@RestController // REST controller trả JSON (Bài 5 - Controller)
@RequestMapping("/staffs") // Prefix API staff (Bài 5 - Controller)
@RequiredArgsConstructor // Inject StaffService qua constructor (Bài 5 - Lombok)
public class StaffController {

    private final StaffService staffService; // Gọi logic staff (Bài 4 - Service)

    @PostMapping // Handle POST /staffs (Bài 5 - Controller)
    @PreAuthorize("hasAuthority('STAFF_CREATE')") // Cần quyền STAFF_CREATE trước khi chạy method (Bài 10 - @PreAuthorize)
    public ApiResponse<StaffResponse> create(@RequestBody @Valid StaffCreateRequest request) { // Nhận request và validate (Bài 4, 5)
        return ApiResponse.success(staffService.create(request)); // Gọi service tạo staff và trả response chuẩn (Bài 15 - ApiResponse)
    }

    @GetMapping // Handle GET /staffs (Bài 5 - Controller)
    @PreAuthorize("hasAuthority('STAFF_READ')") // Cần quyền STAFF_READ (Bài 10 - @PreAuthorize)
    public ApiResponse<List<StaffResponse>> getAll() { // Lấy danh sách staff (Bài 4 - CRUD)
        return ApiResponse.success(staffService.getAll()); // Gọi service và wrap response (Bài 15 - ApiResponse)
    }

    @GetMapping("/{id}") // Handle GET /staffs/{id} (Bài 5 - Controller)
    @PreAuthorize("hasAuthority('STAFF_READ')") // Check quyền đọc trước khi query (Bài 10 - @PreAuthorize)
    @PostAuthorize("hasRole('ADMIN') or returnObject.result.email == authentication.name") // Admin hoặc chính staff đó mới được xem chi tiết (Bài 11 - @PostAuthorize)
    public ApiResponse<StaffResponse> getById(@PathVariable Long id) { // Nhận id từ URL (Bài 5 - Controller)
        return ApiResponse.success(staffService.getById(id)); // Gọi service lấy staff theo id (Bài 4 - CRUD)
    }
}