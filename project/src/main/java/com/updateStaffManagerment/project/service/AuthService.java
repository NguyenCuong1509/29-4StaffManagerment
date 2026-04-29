package com.updateStaffManagerment.project.service;

import com.updateStaffManagerment.project.dto.request.LoginRequest;
import com.updateStaffManagerment.project.dto.request.RegisterRequest;
import com.updateStaffManagerment.project.dto.response.LoginResponse;
import com.updateStaffManagerment.project.entity.Role;
import com.updateStaffManagerment.project.entity.User;
import com.updateStaffManagerment.project.exception.AppException;
import com.updateStaffManagerment.project.exception.ErrorCode;
import com.updateStaffManagerment.project.repository.RoleRepository;
import com.updateStaffManagerment.project.repository.UserRepository;
import com.updateStaffManagerment.project.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public void register(RegisterRequest request){
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.STAFF_EXISTED);
        }
        Set<Role> roles = new HashSet<>();
        if(request.getRoles()!=null){
            for (String roleName : request.getRoles()){
                Role role = roleRepository.findById(roleName)
                        .orElseThrow(()->new AppException(ErrorCode.UNAUTHORIZED));
                roles.add(role);
            }
        }
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode((request.getPassword())))
                .roles(roles)
                .build();
        userRepository.save(user);
    }
    public LoginResponse login(LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user);
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .build();
    }
}
