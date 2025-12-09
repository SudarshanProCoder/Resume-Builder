package com.sudarshandate.resumebuilderapi.controller;

import com.sudarshandate.resumebuilderapi.dto.AuthResponse;
import com.sudarshandate.resumebuilderapi.dto.LoginRequest;
import com.sudarshandate.resumebuilderapi.dto.RegisterRequest;
import com.sudarshandate.resumebuilderapi.service.AuthService;
import com.sudarshandate.resumebuilderapi.service.FileUploadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import static com.sudarshandate.resumebuilderapi.utils.AppConstants.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(AUTH_CONTROLLER)
public class AuthController {

    private final AuthService authService;
    private final FileUploadService fileUploadService;

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

    @PostMapping(UPLOAD_IMAGE)
    public ResponseEntity<?> uploadImage(@RequestPart("image") MultipartFile file) throws IOException {
        Map<String, String> response = fileUploadService.uploadSingleImage(file);
        return ResponseEntity.ok(response);
    }

    @PostMapping(LOGIN)
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request){
        AuthResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate")
    public String testValidationToken(){
        log.debug("validating token");
        return "Token validation is working";
    }


}
