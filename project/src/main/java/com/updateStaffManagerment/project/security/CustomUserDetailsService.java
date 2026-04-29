package com.updateStaffManagerment.project.security;

import com.updateStaffManagerment.project.entity.Permission;
import com.updateStaffManagerment.project.entity.Role;
import com.updateStaffManagerment.project.entity.User;
import com.updateStaffManagerment.project.exception.AppException;
import com.updateStaffManagerment.project.exception.ErrorCode;
import com.updateStaffManagerment.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        for (Role role : user.getRoles()){
            authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getName()));
            for(Permission permission : role.getPermissions()){
                authorities.add(new SimpleGrantedAuthority(permission.getName()));
            }
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
}
