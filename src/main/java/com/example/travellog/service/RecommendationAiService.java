package com.example.travellog.service;


import com.example.travellog.document.TravelRecommendationDto;
import com.example.travellog.document.User;
import com.example.travellog.repository.CityRepository;
import com.example.travellog.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import groovy.util.logging.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class RecommendationAiService {

    private static final Logger log = LoggerFactory.getLogger(RecommendationAiService.class);

    private final ChatClient chatClient;
    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final ObjectMapper objectMapper;

    public RecommendationAiService(ChatClient chatClient,
                                   UserRepository userRepository,
                                   CityRepository cityRepository,
                                   ObjectMapper objectMapper) {
        this.chatClient = chatClient;
        this.userRepository = userRepository;
        this.cityRepository = cityRepository;
        this.objectMapper = objectMapper;
    }


    public List<TravelRecommendationDto> recommendCitiesBasedOnUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        String prompt = buildJsonPrompt(user);


        String rawResponse = chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();  // raw string from AI


        try {
            return objectMapper.readValue(
                    rawResponse,
                    new TypeReference<List<TravelRecommendationDto>>() {}
            );
        } catch (Exception e) {
            log.error("Failed to parse AI JSON response: {}", rawResponse, e);
            throw new RuntimeException("Invalid JSON from AI", e);
        }
    }


    private String buildJsonPrompt(User user) {
        StringBuilder sb = new StringBuilder();

        sb.append("The user has visited:\n");
        user.getVisitedCities().forEach(visited -> {
            sb.append("- ").append(visited.getCityName()).append(": ");
            sb.append(visited.getNotes() != null ? visited.getNotes() : "No description");
            sb.append(visited.isLiked() ? " (Liked)\n" : " (Not liked)\n");
        });

        // Additional instructions
        sb.append("""
                
                Recommend exactly 6 diverse cities for the user to visit, and make sure the list is different every time this is requested.
                
                Each city should be globally diverse and not repeated from common tourist lists. You can include lesser-known or unique travel destinations.
                
                Return an array of objects (no markdown, no backticks) with these exact fields and a short reason why did you recommend that city.
                Please return a JSON array where all numeric values such as exchangeRateUsd are formatted as plain numbers (e.g., 23500.0 instead of 23,500.0).
                Each element must be:
                {
                  "cityName": "string",
                  "countryName":"string",
                  "climate": "string",
                  "exchangeRateUsd": number,
                  "touristSpots": ["string","string","string"]
                  "reason":"string"
                }
                Return only valid JSON array (like [...] ) with no extra text.
                """);
        return sb.toString();
    }
}