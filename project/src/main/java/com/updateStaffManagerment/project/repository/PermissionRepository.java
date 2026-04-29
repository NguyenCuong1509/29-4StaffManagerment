package com.updateStaffManagerment.project.repository;

import com.updateStaffManagerment.project.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, String> {
}