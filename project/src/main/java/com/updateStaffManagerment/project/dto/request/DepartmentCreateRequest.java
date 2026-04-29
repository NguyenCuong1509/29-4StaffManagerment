package com.updateStaffManagerment.project.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DepartmentCreateRequest {
    @NotBlank(message = "Department name is required")
    @Size(max = 100,message = "Department name must be less than 100 characters")
    String name;
}
