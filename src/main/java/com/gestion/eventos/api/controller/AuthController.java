package com.gestion.eventos.api.controller;

import java.util.Collections;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.eventos.api.domain.Role;
import com.gestion.eventos.api.domain.User;
import com.gestion.eventos.api.dto.JwtAuthResponseDto;
import com.gestion.eventos.api.dto.LoginDto;
import com.gestion.eventos.api.dto.RegisterDto;
import com.gestion.eventos.api.mapper.UserMapper;
import com.gestion.eventos.api.repository.RoleRepository;
import com.gestion.eventos.api.repository.UserRepository;
import com.gestion.eventos.api.security.jwt.JwtGenerator;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * AuthController
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtGenerator jwtGenerator;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponseDto> authenticateUser(@Valid @RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthResponseDto(token));
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterDto registerDto) {
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            return ResponseEntity.badRequest().body("El nombre de usuario ya existe");
        }
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            return ResponseEntity.badRequest().body("El correo electrónico ya está registrado");
        }

        User user = userMapper.registerDtoToUser(registerDto);
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Role roles = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Error al obtener el rol"));
        user.setRoles(Collections.singleton(roles));

        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Usuario registrado con éxito");

    }

}
