package com.updateStaffManagerment.project.service;


import com.updateStaffManagerment.project.dto.request.StaffCreateRequest; // Request tạo staff (Bài 5 - DTO)
import com.updateStaffManagerment.project.dto.response.StaffResponse; // Response staff (Bài 5 - DTO)
import com.updateStaffManagerment.project.entity.Department; // Department entity (Bài 2 - Relationship)
import com.updateStaffManagerment.project.entity.Staff; // Staff entity (Bài 2 - Entity)
import com.updateStaffManagerment.project.exception.AppException; // Exception nghiệp vụ (Bài 15 - Exception)
import com.updateStaffManagerment.project.exception.ErrorCode; // ErrorCode (Bài 15 - Exception)
import com.updateStaffManagerment.project.mapper.StaffMapper; // Mapper staff response (Bài 5 - MapStruct)
import com.updateStaffManagerment.project.repository.StaffRepository; // Repository staff (Bài 3 - Repository)
import lombok.RequiredArgsConstructor; // Constructor injection (Bài 5 - Lombok)
import org.springframework.stereotype.Service; // Service bean (Bài 4 - Service)
import org.springframework.transaction.annotation.Transactional; // Transaction ghi DB (Bài 4 - Service)

import java.util.List; // List staff (Bài 4 - CRUD)
import java.util.stream.Collectors; // Map list entity sang DTO (Bài 4 - CRUD)

@Service // Đưa StaffService vào Spring container (Bài 4 - Service)
@RequiredArgsConstructor // Inject dependencies qua constructor (Bài 5 - Lombok)
public class StaffService {

    private final StaffRepository staffRepository; // Query staff từ DB (Bài 3 - Repository)

    private final DepartmentService departmentService; // Lấy department entity theo id (Bài 4 - Service)

    private final StaffMapper staffMapper; // Convert Staff entity sang response DTO (Bài 5 - MapStruct)

    @Transactional // Đảm bảo tạo staff trong transaction (Bài 4 - Service)
    public StaffResponse create(StaffCreateRequest request) { // Tạo staff mới (Bài 4 - CRUD)

        if (staffRepository.existsByEmail(request.getEmail())) { // Check trùng email (Bài 3 - Repository)
            throw new AppException(ErrorCode.STAFF_EXISTED); // Ném lỗi nếu email đã tồn tại (Bài 15 - Exception)
        }

        Department department = departmentService.getEntityById(request.getDepartmentId()); // Lấy department từ DB để gắn FK (Bài 2 - ManyToOne)

        Staff staff = Staff.builder() // Tạo Staff entity (Bài 5 - Lombok)
                .fullName(request.getFullname()) // Gán tên staff từ request (Bài 4 - CRUD)
                .email(request.getEmail()) // Gán email staff từ request (Bài 4 - CRUD)
                .department(department) // Gán quan hệ ManyToOne với Department (Bài 2 - Relationship)
                .build(); // Build entity (Bài 5 - Lombok)

        Staff savedStaff = staffRepository.save(staff); // Lưu staff xuống MySQL (Bài 3 - Repository)

        return staffMapper.toResponse(savedStaff); // Convert entity sang response DTO (Bài 5 - MapStruct)
    }

    public List<StaffResponse> getAll() { // Lấy danh sách staff (Bài 4 - CRUD)
        return staffRepository.findAll() // Query toàn bộ staff (Bài 3 - Repository)
                .stream() // Chuyển sang stream (Bài 4 - CRUD)
                .map(staffMapper::toResponse) // Convert từng staff sang DTO (Bài 5 - MapStruct)
                .collect(Collectors.toList()); // Gom lại thành list (Bài 4 - CRUD)
    }

    public StaffResponse getById(Long id) { // Lấy staff theo id (Bài 4 - CRUD)
        Staff staff = staffRepository.findById(id) // Query staff theo id (Bài 3 - Repository)
                .orElseThrow(() -> new AppException(ErrorCode.STAFF_NOT_FOUND)); // Không có thì ném lỗi (Bài 15 - Exception)

        return staffMapper.toResponse(staff); // Convert entity sang response DTO (Bài 5 - MapStruct)
    }
}
