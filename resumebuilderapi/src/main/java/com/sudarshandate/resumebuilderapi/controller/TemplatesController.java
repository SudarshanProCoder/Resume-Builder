package com.sudarshandate.resumebuilderapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sudarshandate.resumebuilderapi.service.TemplatesService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static com.sudarshandate.resumebuilderapi.utils.AppConstants.*;

import java.util.Map;;

@RestController
@RequiredArgsConstructor
@RequestMapping(TEMPLATES)
@Slf4j
public class TemplatesController {

    private final TemplatesService templatesService;

    @GetMapping
    public ResponseEntity<?> getTemplates(Authentication authentication) {

        Map<String, Object> templates = templatesService.getTemplates(authentication.getPrincipal());

        return ResponseEntity.ok(templates);
    }
}
