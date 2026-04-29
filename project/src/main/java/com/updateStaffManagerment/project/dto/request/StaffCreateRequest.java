package com.updateStaffManagerment.project.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StaffCreateRequest {
    @NotBlank(message = "Full name is required")
    @Size(max = 100,message = "Fullname must be less than 100 characters")
    String fullname;
    @NotBlank(message = "Email is required")
    @Email
    String email;
    @NotNull
    Long departmentId;
}
