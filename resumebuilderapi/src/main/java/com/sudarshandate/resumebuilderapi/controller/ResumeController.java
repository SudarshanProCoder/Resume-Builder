package com.sudarshandate.resumebuilderapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sudarshandate.resumebuilderapi.document.Resume;
import com.sudarshandate.resumebuilderapi.dto.CreateResumeRequest;
import com.sudarshandate.resumebuilderapi.service.FileUploadService;
import com.sudarshandate.resumebuilderapi.service.ResumeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.sudarshandate.resumebuilderapi.utils.AppConstants.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(RESUME_CONTROLLER)
@RequiredArgsConstructor
@Slf4j
public class ResumeController {

    private final ResumeService resumeService;
    private final FileUploadService fileUploadService;

    @PostMapping
    public ResponseEntity<?> createResume(@Valid @RequestBody CreateResumeRequest request,
            Authentication authentication) {
        Resume newResume = resumeService.createResume(request, authentication.getPrincipal());

        return ResponseEntity.status(HttpStatus.CREATED).body(newResume);
    }

    @GetMapping
    public ResponseEntity<?> getUserResumes(Authentication authentication) {

        List<Resume> resumes = resumeService.getUserResumes(authentication.getPrincipal());

        return ResponseEntity.ok(resumes);
    }

    @GetMapping(ID)
    public ResponseEntity<?> getResumeById(@PathVariable String id, Authentication authentication) {

        Resume exestingResume = resumeService.getResumeById(id, authentication.getPrincipal());

        return ResponseEntity.ok(exestingResume);
    }

    @PutMapping(ID)
    public ResponseEntity<?> updateResume(@PathVariable String id, @RequestBody Resume updatedtData,
            Authentication authentication) {

        Resume updatedResume = resumeService.updatedResume(id, updatedtData, authentication.getPrincipal());

        return ResponseEntity.ok(updatedResume);
    }

    @PutMapping(UPLOAD)
    public ResponseEntity<?> uploadResumeImages(@PathVariable String id,
            @RequestPart(value = "thumbnail", required = true) MultipartFile thumbnail,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
            HttpServletRequest request, Authentication authentication) throws IOException {

        Map<String, String> response = fileUploadService.uploadResumeImages(id, authentication.getPrincipal(),
                thumbnail, profileImage);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(ID)
    public ResponseEntity<?> deleteResume(@PathVariable String id, Authentication authentication) {

        resumeService.deleteResume(id, authentication.getPrincipal());

        return ResponseEntity.ok(Map.of("message", "Resume deleted successfully"));
    }
}
