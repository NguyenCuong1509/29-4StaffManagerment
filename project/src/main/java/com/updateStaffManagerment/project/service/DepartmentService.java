package com.updateStaffManagerment.project.service;

import com.updateStaffManagerment.project.dto.request.DepartmentCreateRequest;
import com.updateStaffManagerment.project.dto.response.DepartmentResponse;
import com.updateStaffManagerment.project.entity.Department;
import com.updateStaffManagerment.project.exception.AppException;
import com.updateStaffManagerment.project.exception.ErrorCode;
import com.updateStaffManagerment.project.mapper.DepartmentMapper;
import com.updateStaffManagerment.project.repository.DepartmentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Transactional
    public DepartmentResponse create (DepartmentCreateRequest request){
        if (departmentRepository.existsByName(request.getName())) {
            throw new AppException((ErrorCode.DEPARTMENT_EXISTTED));
        }
        Department department = departmentMapper.toEntity((request));

        Department savedDepartment = departmentRepository.save(department);
        return departmentMapper.toResponse(savedDepartment);
    }
    public List<DepartmentResponse> getAll(){
        return departmentRepository.findAll()
                .stream()
                .map(departmentMapper::toResponse)
                .collect((Collectors.toList()));
    }
    public Department getEntityById(Long id){
        return departmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
    }
}
