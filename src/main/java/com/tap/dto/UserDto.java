package com.tap.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UserDto {
    private UUID userId;
    private String username;
    private String email;
    private String role;
    private LocalDateTime createdAt;
    private Boolean authorization;
    private Boolean isVerified; // derived from subclass if available

}
