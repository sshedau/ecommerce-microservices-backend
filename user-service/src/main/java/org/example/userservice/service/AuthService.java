package org.example.userservice.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.userservice.dto.CreateUserDto;
import org.example.userservice.dto.LoginDto;
import org.example.userservice.dto.LoginResponseDto;
import org.example.userservice.dto.RegisterUserResponseDto;
import org.example.userservice.entities.Role;
import org.example.userservice.entities.User;
import org.example.userservice.repository.UserRepository;
import org.example.userservice.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Data
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public RegisterUserResponseDto registerUser(
            CreateUserDto createUserDto) {

        User user = new User();

        user.setName(createUserDto.getName());
        user.setEmail(createUserDto.getEmail());
        user.setPassword(
                passwordEncoder.encode(createUserDto.getPassword())
        );
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);

        return new RegisterUserResponseDto(
                savedUser.getId(),
                savedUser.getName()
        );
    }

    public LoginResponseDto login(LoginDto loginDto) {

        // 1. Authenticate email + password
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginDto.getEmail(),
                                loginDto.getPassword()
                        )
                );

        // 2. Get actual User from database
        User user = userRepository
                .findByEmail(loginDto.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        // 3. Generate JWT containing userId + role
        String jwtToken =
                jwtService.generateJwtToken(user);

        return new LoginResponseDto(jwtToken);
    }
}