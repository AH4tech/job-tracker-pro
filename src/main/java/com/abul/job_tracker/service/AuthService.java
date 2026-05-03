package com.abul.job_tracker.service;

import com.abul.job_tracker.dto.AuthRequest;
import com.abul.job_tracker.dto.AuthResponse;
import com.abul.job_tracker.exception.EmailAlreadyExistsException;
import com.abul.job_tracker.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import com.abul.job_tracker.model.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.abul.job_tracker.repository.UserRepository;
import com.abul.job_tracker.security.JwtUtil;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(AuthRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already Registered " +  request.getEmail());
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(
                token,
                user.getEmail(),
                user.getName()
        );
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email " + request.getEmail()));
        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(
                token,
                user.getEmail(),
                user.getName()
        );
    }

}




























