package com.courseplatform.service;

import com.courseplatform.dto.LoginResponse;
import com.courseplatform.dto.RegisterResponse;
import com.courseplatform.entity.User;
import com.courseplatform.exception.ConflictException;
import com.courseplatform.exception.UnauthorizedException;
import com.courseplatform.repository.UserRepository;
import com.courseplatform.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public RegisterResponse register(String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("Email already exists", "An account with this email already exists");
        }

        User user = new User(email, passwordEncoder.encode(password));
        user = userRepository.save(user);

        return new RegisterResponse(user.getId(), user.getEmail(), "User registered successfully");
    }

    public LoginResponse login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        String token = tokenProvider.generateToken(email);
        return new LoginResponse(token, email, tokenProvider.getExpirationSeconds());
    }
}
