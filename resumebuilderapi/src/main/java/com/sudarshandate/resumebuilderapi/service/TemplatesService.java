package com.sudarshandate.resumebuilderapi.service;

import static com.sudarshandate.resumebuilderapi.utils.AppConstants.PREMIUM;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.sudarshandate.resumebuilderapi.dto.AuthResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemplatesService {

    private final AuthService authService;

    public Map<String, Object> getTemplates(Object principalObject) {

        AuthResponse response = authService.getProfile(principalObject);

        List<String> availableTeamplates;

        Boolean isPremium = PREMIUM.equalsIgnoreCase(response.getSubscriptionPlan());

        if (isPremium) {
            availableTeamplates = List.of("01", "02", "03");
        } else {
            availableTeamplates = List.of("01");
        }

        Map<String, Object> restrictions = new HashMap<>();
        restrictions.put("availableTeamplates", availableTeamplates);
        restrictions.put("allTemplates", List.of("01", "02", "03"));
        restrictions.put("subscriptionPlan", response.getSubscriptionPlan());
        restrictions.put("isPremium", isPremium);

        return restrictions;
    }
}
