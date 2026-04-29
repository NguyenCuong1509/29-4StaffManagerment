package com.updateStaffManagerment.project.mapper;

import com.updateStaffManagerment.project.config.CentralMapperConfig;
import com.updateStaffManagerment.project.dto.response.StaffResponse;
import com.updateStaffManagerment.project.entity.Staff;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CentralMapperConfig.class)
public interface StaffMapper {
    @Mapping(source = "department.id",target = "departmentId")
    @Mapping(source = "department.name",target = "departmentName")
    StaffResponse toResponse(Staff staff);
}
