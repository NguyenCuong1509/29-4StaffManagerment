package com.updateStaffManagerment.project.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StaffResponse {
    Long id;
    String fullname;
    String email;
    Long departmentId;
    String departmentName;
}
