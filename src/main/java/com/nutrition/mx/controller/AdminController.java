package com.nutrition.mx.controller;

import com.nutrition.mx.dto.request.CreateUserRequest;
import com.nutrition.mx.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> createAdmin(@Valid @RequestBody CreateUserRequest request, Authentication authentication){
        return userService.createAdmin(request, authentication.getName());
    }

    @PostMapping("/super")
    public ResponseEntity<?> createSuperAdmin(@Valid @RequestBody CreateUserRequest request, Authentication authentication){
        return userService.createSuperAdmin(request, authentication.getName());
    }
}
