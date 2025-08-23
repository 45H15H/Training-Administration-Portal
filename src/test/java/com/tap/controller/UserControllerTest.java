package com.tap.controller;

import com.tap.controllers.UserController;
import com.tap.dto.UserDto;
import com.tap.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private UUID userId;
    private UserDto userDto;

    @BeforeEach
    void setUpUser() {
        userId = UUID.randomUUID();
//        userDto = new UserDto();
//        userDto.setUserId(userId);
//        userDto.setUsername("Alice");
//        userDto.setEmail("alice@example.com");
//        userDto.setRole("STUDENT");
    }

    @Test
    void testGetUserById_ReturnsUserDto() {
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        userDto.setUsername("Alice");
        userDto.setEmail("alice@example.com");
        userDto.setRole("STUDENT");

        when(userService.getUserById(userId)).thenReturn(userDto);

        ResponseEntity<UserDto> response = userController.getUserById(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userDto, response.getBody());
    }

    @Test
    void testGetUserById_UserNotFound_ThrowsException() {
        when(userService.getUserById(userId)).thenThrow(new RuntimeException("User not found"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userController.getUserById(userId);
        });

        assertEquals("User not found", exception.getMessage());
    }


}
