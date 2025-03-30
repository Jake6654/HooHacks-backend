package com.example.travellog.controller;


import com.example.travellog.document.User;
import com.example.travellog.document.VisitedCity;
import com.example.travellog.document.VisitedCityDto;
import com.example.travellog.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
public class VisitedCityController {

    private final UserRepository userRepository;

    public VisitedCityController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/submit")
    public ResponseEntity<?> addVisitedCityForm(
            @RequestBody VisitedCityDto dto,
            Authentication authentication
    ) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }


        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found by email: " + email));


        VisitedCity newVisited = new VisitedCity();
        newVisited.setCityName(dto.getCityName());
        newVisited.setLat(dto.getLat());
        newVisited.setLng(dto.getLng());
        newVisited.setNotes(dto.getNotes());
        newVisited.setLiked(dto.isLiked());
        newVisited.setCountryName(dto.getCountryName());

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        newVisited.setDate(LocalDateTime.parse(dto.getDate(), formatter));

        user.getVisitedCities().add(newVisited);
        userRepository.save(user);


        return ResponseEntity.ok(newVisited);

    }

    @GetMapping("visitedcities")
    public ResponseEntity<?> getVisitedCities(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        String email = authentication.getName();


        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }


        return ResponseEntity.ok(userOpt.get().getVisitedCities());
    }

    @GetMapping("/visitedcities/{id}")
    public ResponseEntity<?> getVisitedCityById(
            @PathVariable String id,
            Authentication authentication
    ) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }


        String email = authentication.getName();


        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }


        return userOpt.get().getVisitedCities().stream()
                .filter(city -> id.equals(city.getId()))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());


    }

    @DeleteMapping("/visitedcities/{id}")
    public ResponseEntity<?> deleteVisitedCityById(
            @PathVariable String id,
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        String email = authentication.getName();
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();

        boolean removed = user.getVisitedCities().removeIf(city -> id.equals(city.getId()));

        if (removed) {
            userRepository.save(user); // 변경사항 저장
            return ResponseEntity.ok(Map.of("message", "Visited city deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "City with ID " + id + " not found"));
        }
    }
}