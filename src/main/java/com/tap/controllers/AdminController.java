package com.tap.controllers;

import com.tap.dto.UserCreationDto;
import com.tap.dto.UserDto;
import com.tap.services.UserService;
import com.tap.mappers.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Admin endpoints without separate admin table. Any base User (not Student/Instructor) gains ROLE_ADMIN via CustomUserDetails.
 */
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final UserMapper userMapper;

    public AdminController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/users")
    public ResponseEntity<UserDto> createAdminUser(@RequestBody UserCreationDto dto) {
        // Creates a base User which will automatically have ROLE_ADMIN
        var user = userMapper.toUser(dto);
        UserDto created = userService.createUser(user);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> listUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PatchMapping("/users/{userId}/authorization")
    public ResponseEntity<UserDto> setAuthorization(@PathVariable UUID userId, @RequestBody AuthorizationBody body) {
        return ResponseEntity.ok(userService.updateAuthorization(userId, body.authorization()));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    public record AuthorizationBody(Boolean authorization) {}
}
