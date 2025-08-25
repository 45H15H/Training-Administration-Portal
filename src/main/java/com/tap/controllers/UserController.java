package com.tap.controllers;

import com.tap.dto.UserDto;
import com.tap.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) { this.userService = userService; }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID userId) {
        UserDto user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    // Legacy admin operations removed in favor of /api/admin/**.
}
