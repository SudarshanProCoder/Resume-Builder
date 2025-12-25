package com.sudarshandate.resumebuilderapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sudarshandate.resumebuilderapi.document.Resume;

public interface ResumeRepository extends MongoRepository<Resume, String> {

    List<Resume> findByUserIdOrderByUpdatedAtDesc(String userId);

    Optional<Resume> findByUserIdAndId(String userId, String id);

}
