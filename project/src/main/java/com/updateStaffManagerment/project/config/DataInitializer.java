package com.updateStaffManagerment.project.config;

import com.updateStaffManagerment.project.entity.Permission;
import com.updateStaffManagerment.project.entity.Role;
import com.updateStaffManagerment.project.entity.User;
import com.updateStaffManagerment.project.repository.PermissionRepository;
import com.updateStaffManagerment.project.repository.RoleRepository;
import com.updateStaffManagerment.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData() {
        return args -> {

            Permission departmentCreate = createPermissionIfNotExists(
                    "DEPARTMENT_CREATE",
                    "Create department"
            );

            Permission departmentRead = createPermissionIfNotExists(
                    "DEPARTMENT_READ",
                    "Read department"
            );

            Permission staffCreate = createPermissionIfNotExists(
                    "STAFF_CREATE",
                    "Create staff"
            );

            Permission staffRead = createPermissionIfNotExists(
                    "STAFF_READ",
                    "Read staff"
            );

            Role adminRole = roleRepository.findById("ADMIN")
                    .orElseGet(() -> Role.builder()
                            .name("ADMIN")
                            .description("Administrator")
                            .build());

            adminRole.setDescription("Administrator");

            adminRole.setPermissions(Set.of(
                    departmentCreate,
                    departmentRead,
                    staffCreate,
                    staffRead
            ));

            roleRepository.save(adminRole);

            if (!userRepository.existsByUsername("admin")) {
                User admin = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("12345678"))
                        .roles(Set.of(adminRole))
                        .build();

                userRepository.save(admin);
            }
        };
    }

    private Permission createPermissionIfNotExists(String name, String description) {
        return permissionRepository.findById(name)
                .orElseGet(() -> permissionRepository.save(
                        Permission.builder()
                                .name(name)
                                .description(description)
                                .build()
                ));
    }
}