package com.updateStaffManagerment.project.mapper;

import com.updateStaffManagerment.project.config.CentralMapperConfig;
import com.updateStaffManagerment.project.dto.request.DepartmentCreateRequest;
import com.updateStaffManagerment.project.dto.response.DepartmentResponse;
import com.updateStaffManagerment.project.entity.Department;
import org.mapstruct.Mapper;

@Mapper(config = CentralMapperConfig.class)
public interface DepartmentMapper {
    Department toEntity(DepartmentCreateRequest request);
    DepartmentResponse toResponse (Department department);
}
