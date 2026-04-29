package com.updateStaffManagerment.project.controller;

import com.updateStaffManagerment.project.dto.request.DepartmentCreateRequest;
import com.updateStaffManagerment.project.dto.response.ApiResponse;
import com.updateStaffManagerment.project.dto.response.DepartmentResponse;
import com.updateStaffManagerment.project.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.PanelUI;
import java.security.PublicKey;
import java.util.List;

@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @PostMapping
    @PreAuthorize("hasAuthority('DEPARTMENT_CREATE')")
    public ApiResponse<DepartmentResponse> create(@RequestBody @Valid DepartmentCreateRequest request){
        return ApiResponse.success(departmentService.create(request));
    }
    @GetMapping
    @PreAuthorize("hasAuthority('DEPARTMENT_READ')")
    public ApiResponse<List<DepartmentResponse>> getAll(){
        return ApiResponse.success(departmentService.getAll());
    }
}
