package com.tap.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class StudentDto {
    private UUID userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private String phoneNumber;
    private Integer age;
    private Boolean isVerified;
}
