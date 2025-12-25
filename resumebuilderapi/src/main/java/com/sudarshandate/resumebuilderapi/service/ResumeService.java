package com.sudarshandate.resumebuilderapi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sudarshandate.resumebuilderapi.document.Resume;
import com.sudarshandate.resumebuilderapi.dto.AuthResponse;
import com.sudarshandate.resumebuilderapi.dto.CreateResumeRequest;
import com.sudarshandate.resumebuilderapi.repository.ResumeRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResumeService {

    private final AuthService authService;

    private final ResumeRepository resumeRepository;

    public Resume createResume(@Valid CreateResumeRequest request, Object principalObject) {

        Resume newResume = new Resume();

        AuthResponse response = authService.getProfile(principalObject);

        newResume.setUserId(response.getId());
        newResume.setTitle(request.getTitle());

        setDefaultResumeData(newResume);

        return resumeRepository.save(newResume);
    }

    public List<Resume> getUserResumes(Object principalObject) {

        AuthResponse response = authService.getProfile(principalObject);

        List<Resume> resumes = resumeRepository.findByUserIdOrderByUpdatedAtDesc(response.getId());

        return resumes;
    }

    public Resume getResumeById(String resumeId, Object principalObject) {

        AuthResponse response = authService.getProfile(principalObject);

        Resume exestingResume = resumeRepository.findByUserIdAndId(response.getId(), resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        return exestingResume;
    }

    public Resume updatedResume(String resumeId, Resume updatedData, Object principalObject) {

        AuthResponse response = authService.getProfile(principalObject);

        Resume exestingResume = resumeRepository.findByUserIdAndId(response.getId(), resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        exestingResume.setTitle(updatedData.getTitle());
        exestingResume.setThumbnailLink(updatedData.getThumbnailLink());
        exestingResume.setTemplate(updatedData.getTemplate());
        exestingResume.setProfileInfo(updatedData.getProfileInfo());
        exestingResume.setContactInfo(updatedData.getContactInfo());
        exestingResume.setWorkExperiences(updatedData.getWorkExperiences());
        exestingResume.setEducation(updatedData.getEducation());
        exestingResume.setSkills(updatedData.getSkills());
        exestingResume.setProjects(updatedData.getProjects());
        exestingResume.setCertifications(updatedData.getCertifications());
        exestingResume.setLanguages(updatedData.getLanguages());
        exestingResume.setInterests(updatedData.getInterests());

        resumeRepository.save(exestingResume);

        return exestingResume;

    }

    public void deleteResume(String resumeId, Object principalObject) {

        AuthResponse response = authService.getProfile(principalObject);

        Resume exsitingResume = resumeRepository.findByUserIdAndId(response.getId(), resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        resumeRepository.delete(exsitingResume);

    }

    private void setDefaultResumeData(Resume newResume) {

        newResume.setProfileInfo(new Resume.ProfileInfo());
        newResume.setContactInfo(new Resume.ContactInfo());
        newResume.setWorkExperiences(new ArrayList<>());
        newResume.setEducation(new ArrayList<>());
        newResume.setSkills(new ArrayList<>());
        newResume.setProjects(new ArrayList<>());
        newResume.setCertifications(new ArrayList<>());
        newResume.setLanguages(new ArrayList<>());
        newResume.setInterests(new ArrayList<>());
    }
}
