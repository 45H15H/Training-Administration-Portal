package com.tap.services;

import com.tap.dto.UserDto;
import com.tap.entities.User;
import com.tap.entities.Student;
import com.tap.entities.Instructor;
import com.tap.repositories.StudentRepository;
import com.tap.repositories.InstructorRepository;
import com.tap.mappers.UserMapper;
import com.tap.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper,
                       StudentRepository studentRepository, InstructorRepository instructorRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.studentRepository = studentRepository;
        this.instructorRepository = instructorRepository;
    }

    @Transactional
    public UserDto createUser(User user) {
        // Basic user creation, primarily for admins
        userRepository.findByUsername(user.getUsername()).ifPresent(u -> {
            throw new IllegalArgumentException("Username already exists");
        });
        userRepository.findByEmail(user.getEmail()).ifPresent(u -> {
            throw new IllegalArgumentException("Email already exists");
        });

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    public UserDto getUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return userMapper.toDto(user);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<UserDto> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<UserDto> getAllInstructors() {
        return instructorRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDto updateAuthorization(UUID userId, Boolean authorization) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        user.setAuthorization(authorization);

        // Propagate to subclass record if present to keep isVerified in sync
        // Students: isVerified mirrors authorization
        studentRepository.findById(userId).ifPresent(student -> {
            student.setIsVerified(authorization);
            studentRepository.save(student);
        });
        // Instructors: isVerified mirrors authorization (admin approved)
        instructorRepository.findById(userId).ifPresent(instructor -> {
            instructor.setIsVerified(authorization);
            instructorRepository.save(instructor);
        });

        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    @Transactional
    public void deleteUser(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        // Delete subtype rows first to satisfy FK constraints
        studentRepository.findById(userId).ifPresent(s -> studentRepository.deleteById(userId));
        instructorRepository.findById(userId).ifPresent(i -> instructorRepository.deleteById(userId));
        userRepository.deleteById(userId);
    }
}
