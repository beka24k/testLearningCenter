package com.example.demo.controller;

import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.email.EmailService;
import com.example.demo.model.AppUser;
import com.example.demo.model.Role;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final EmailService emailService;

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody AppUser user) {
        if (user.getUsername() == null || user.getPassword() == null) {
            throw new RuntimeException("Имя пользователя и пароль обязательны");
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Пользователь уже существует");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null)
            user.setRole(Role.USER);
        userRepository.save(user);

        // ✅ Отправка письма
        String subject = "Добро пожаловать в Learning Center!";
        String message = "Здравствуйте, " + user.getUsername() + "! Вы успешно зарегистрировались в системе.";
        emailService.sendEmail(user.getUsername(), subject, message); // username должен быть email

        String token = jwtUtils.generateToken(user.getUsername());
        return Map.of("token", token);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody AppUser user) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        } catch (Exception e) {
            throw new RuntimeException("Неверный логин или пароль");
        }

        String token = jwtUtils.generateToken(user.getUsername());
        return Map.of("token", token);
    }
}
