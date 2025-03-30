package com.example.travellog.controller;

import com.example.travellog.document.User;
import com.example.travellog.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ViewController {


    private final UserRepository userRepository;

    public ViewController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", user.getUserName());
        userInfo.put("email", user.getEmail());
        userInfo.put("photo", user.getPhotoUrl());
        return ResponseEntity.ok(userInfo);
    }
}
