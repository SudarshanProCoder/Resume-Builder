package com.sudarshandate.resumebuilderapi.controller;

import com.sudarshandate.resumebuilderapi.dto.AuthResponse;
import com.sudarshandate.resumebuilderapi.dto.RegisterRequest;
import com.sudarshandate.resumebuilderapi.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import static com.sudarshandate.resumebuilderapi.utils.AppConstants.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(AUTH_CONTROLLER)
public class AuthController {

    private final AuthService authService;

    @PostMapping(REGISTER)
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request){
        AuthResponse authResponse = authService.register(request);
        log.info("Response from register method is {}", authResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
    }

    @GetMapping(VERIFY_EMAIL)
    public ResponseEntity<?> verifyEmail(@RequestParam String token){
        authService.verifyEmail(token);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "email verified"));
    }
}
