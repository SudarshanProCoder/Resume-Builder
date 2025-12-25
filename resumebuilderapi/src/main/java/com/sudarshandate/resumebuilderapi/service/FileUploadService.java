package com.sudarshandate.resumebuilderapi.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.sudarshandate.resumebuilderapi.document.Resume;
import com.sudarshandate.resumebuilderapi.dto.AuthResponse;
import com.sudarshandate.resumebuilderapi.repository.ResumeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final Cloudinary cloudinary;
    private final AuthService authService;
    private final ResumeRepository resumeRepository;

    public Map<String, String> uploadSingleImage(MultipartFile file) throws IOException {

        Map<String, Object> imageUploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "resource_type", "image"));
        log.info("Inside FileUploadService uploadSingleImage {}", imageUploadResult);
        return Map.of("imageUrl", imageUploadResult.get("secure_url").toString());

    }

    public Map<String, String> uploadResumeImages(String id, Object principalObject, MultipartFile thumbnail,
            MultipartFile profileImage) throws IOException {

        AuthResponse response = authService.getProfile(principalObject);

        Resume exestingResume = resumeRepository.findByUserIdAndId(response.getId(), id)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        Map<String, String> returnValue = new HashMap<>();
        Map<String, String> uploadResult;

        if (Objects.nonNull(thumbnail)) {

            uploadResult = uploadSingleImage(thumbnail);
            exestingResume.setThumbnailLink(uploadResult.get("imageUrl"));
            returnValue.put("thumbnailLink", uploadResult.get("imageUrl"));

        }
        if (Objects.nonNull(profileImage)) {

            uploadResult = uploadSingleImage(profileImage);
            if (Objects.isNull(exestingResume.getProfileInfo())) {
                exestingResume.setProfileInfo(new Resume.ProfileInfo());
            }
            exestingResume.getProfileInfo().setProfilePreviewUrl(uploadResult.get("imageUrl"));
            returnValue.put("profilePreviewUrl", uploadResult.get("imageUrl"));

        }

        resumeRepository.save(exestingResume);
        returnValue.put("message", "Images uploaded successfully");

        return returnValue;
    }
}
