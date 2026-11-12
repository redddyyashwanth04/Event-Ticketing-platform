package com.example.Ticketing_project.service;

import com.example.Ticketing_project.dto.JwtResponse;
import com.example.Ticketing_project.dto.LoginRequest;
import com.example.Ticketing_project.dto.RegisterRequest;
import com.example.Ticketing_project.entity.Role;
import com.example.Ticketing_project.entity.User;
import com.example.Ticketing_project.exception.BadRequestException;
import com.example.Ticketing_project.repository.RoleRepository;
import com.example.Ticketing_project.repository.UserRepository;
import com.example.Ticketing_project.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    // 1. REGISTER LOGIC
    public String registerUser(RegisterRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Error: Email is already in use!");
        }

        // Create user entity and encode password using BCrypt
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Assign Role (Defaults to ROLE_ATTENDEE if none provided)
        Set<Role> roles = new HashSet<>();
        String strRole = request.getRole();

        Role assignedRole;
        if (strRole == null || strRole.trim().isEmpty()) {
            assignedRole = roleRepository.findByName("ROLE_ATTENDEE")
                    .orElseThrow(() -> new RuntimeException("Error: Default role ROLE_ATTENDEE not found."));
        } else {
            assignedRole = roleRepository.findByName(strRole)
                    .orElseThrow(() -> new RuntimeException("Error: Role " + strRole + " not found."));
        }
        roles.add(assignedRole);
        user.setRoles(roles);

        userRepository.save(user);
        return "User registered successfully!";
    }

    // 2. LOGIN LOGIC
    public JwtResponse authenticateUser(LoginRequest request) {
        // Authenticate credentials using Spring Security's AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT Token using JwtUtils
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtUtils.generateToken(userDetails);

        // Fetch user entity from database to populate response details
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return JwtResponse.builder()
                .token(jwt)
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .roles(roles)
                .build();
    }
}