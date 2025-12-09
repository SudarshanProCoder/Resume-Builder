package com.sudarshandate.resumebuilderapi.service;

import com.sudarshandate.resumebuilderapi.document.User;
import com.sudarshandate.resumebuilderapi.dto.AuthResponse;
import com.sudarshandate.resumebuilderapi.dto.LoginRequest;
import com.sudarshandate.resumebuilderapi.dto.RegisterRequest;
import com.sudarshandate.resumebuilderapi.exception.ResourceExistsException;
import com.sudarshandate.resumebuilderapi.repository.UserRepository;
import com.sudarshandate.resumebuilderapi.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Value("${app.base.url}")
    private String appBaseUrl;

    public AuthResponse register(RegisterRequest request) {
        log.info("Inside AuthService: register() {}", request);

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceExistsException("Email already exists");
        }

        User newUser = toDocument(request);

        userRepository.save(newUser);

        sendVerificationEmail(newUser);

        return toResponse(newUser);
    }

    private void sendVerificationEmail(User newUser) {
        log.info("Inside AuthService: sendVerificationEmail() {}", newUser);
        try {
            String link = appBaseUrl + "/api/auth/verify-email?token=" + newUser.getVerificationToken();

            String html = "<div style='font-family:sans-serif; background-color:#f4f4f4; padding:20px;'>"
                    + "<div style='max-width:600px; margin:0 auto; background:#ffffff; padding:30px; border-radius:10px; box-shadow:0 2px 8px rgba(0,0,0,0.1);'>"
                    + "<h2 style='color:#333333; text-align:center;'>Verify Your Email</h2>"
                    + "<p style='font-size:16px; color:#555555;'>Hi " + newUser.getName() + ",</p>"
                    + "<p style='font-size:16px; color:#555555;'>Please confirm your email to activate your account by clicking the button below:</p>"
                    + "<p style='text-align:center;'>"
                    + "<a href='" + link + "' "
                    + "style='display:inline-block; padding:12px 24px; background-color:#6366f1; color:#ffffff; text-decoration:none; border-radius:6px; font-weight:bold;'>"
                    + "Verify Email</a></p>"
                    + "<p style='font-size:14px; color:#555555; text-align:center;'>Or copy this link into your browser:</p>"
                    + "<p style='font-size:14px; color:#555555; word-break:break-all; text-align:center;'>" + link + "</p>"
                    + "<p style='font-size:14px; color:#999999; text-align:center;'>This link expires in 24 hours.</p>"
                    + "<p style='font-size:14px; color:#555555; text-align:center;'>Thank you!</p>"
                    + "</div>"
                    + "</div>";
            emailService.sendHtmlEmail(newUser.getEmail(), "Verify Email", html);
        } catch (Exception e) {
            log.error("Exception occured at send Verification Email: ", e.getMessage());
            throw new RuntimeException("Error sending verification email");
        }
    }

    private AuthResponse toResponse(User newUser) {
        return AuthResponse.builder()
                .id(newUser.getId())
                .name(newUser.getName())
                .email(newUser.getEmail())
                .profileImageUrl(newUser.getProfileImageUrl())
                .emailVerified(newUser.isEmailVerified())
                .subscriptionPlan(newUser.getSubscriptionPlan())
                .createdAt(newUser.getCreatedAt())
                .updatedAt(newUser.getUpdatedAt())
                .build();
    }

    private User toDocument(RegisterRequest request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .profileImageUrl(request.getProfileImageUrl())
                .subscriptionPlan("Basic")
                .emailVerified(false)
                .verificationToken(UUID.randomUUID().toString())
                .verficationExpires(LocalDateTime.now().plusHours(24))
                .build();
    }

    public void verifyEmail(String token) {
        log.info("Inside AuthService: verifyEmail() {}", token);
        User user = userRepository.findByVerificationToken(token).orElseThrow(() -> new RuntimeException("Verification token not found"));

        if (user.getVerficationExpires() != null && user.getVerficationExpires().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Verification token expired");
        }

        user.setEmailVerified(true);
        user.setVerificationToken(null);
        user.setVerficationExpires(null);
        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request){
        User existingUser = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid email or password"));

        if(!passwordEncoder.matches(request.getPassword(), existingUser.getPassword())) {
            throw new UsernameNotFoundException("Invalid email or password");
        }

        if(!existingUser.isEmailVerified()) {
            throw new RuntimeException("Please verify your email");
        }

        String token = jwtUtil.generateJwtToken(existingUser.getId());

        AuthResponse response = toResponse(existingUser);
        response.setVerificationToken(token);
        return response;
    }
}
