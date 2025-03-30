package com.example.travellog.controller;

import com.example.travellog.document.TravelRecommendationDto;
import com.example.travellog.service.RecommendationAiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RecommendationController {

    private final RecommendationAiService aiService;

    public RecommendationController(RecommendationAiService aiService) {
        this.aiService = aiService;
    }

    @GetMapping("/rec")
    public ResponseEntity<List<TravelRecommendationDto>> recommendCitiesJson(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = authentication.getName();

        List<TravelRecommendationDto> dtoList = aiService.recommendCitiesBasedOnUser(email);

        return ResponseEntity.ok(dtoList);
    }
}
