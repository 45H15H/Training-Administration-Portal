//package com.tap.services;
//
//import com.tap.dto.InstructorCreationDto;
//import com.tap.dto.InstructorDto;
//import com.tap.dto.InstructorResumeDto;
//import com.tap.entities.Instructor;
//import com.tap.entities.InstructorResume;
//import com.tap.exceptions.ResourceNotFoundException;
//import com.tap.exceptions.DuplicateResourceException;
//import com.tap.repositories.UserRepository;
//import com.tap.mappers.UserMapper;
//import com.tap.repositories.InstructorRepository;
//import com.tap.repositories.InstructorResumeRepository;
//import jakarta.transaction.Transactional;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//@Service
//public class InstructorService {
//
//    private final InstructorRepository instructorRepository;
//    private final UserMapper userMapper;
//    private final InstructorResumeRepository resumeRepository;
//    private final UserRepository userRepository;
//    private final Path fileStorageLocation;
//
//    public InstructorService(InstructorRepository instructorRepository, UserMapper userMapper,
//                             InstructorResumeRepository resumeRepository, UserRepository userRepository,
//                             @Value("${file.upload-dir}") String uploadDir) {
//        this.instructorRepository = instructorRepository;
//        this.userMapper = userMapper;
//        this.resumeRepository = resumeRepository;
//        this.userRepository = userRepository;
//        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
//        try {
//            Files.createDirectories(this.fileStorageLocation);
//        } catch (Exception ex) {
//            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
//        }
//    }
//
//    @Transactional
//    public InstructorDto createInstructor(InstructorCreationDto dto) {
//        userRepository.findByUsername(dto.getUsername()).ifPresent(u -> {
//            throw new DuplicateResourceException("Username already exists: " + dto.getUsername());
//        });
//        userRepository.findByEmail(dto.getEmail()).ifPresent(u -> {
//            throw new DuplicateResourceException("Email already exists: " + dto.getEmail());
//        });
//        Instructor instructor = new Instructor();
//        instructor.setUsername(dto.getUsername());
//        instructor.setPassword(dto.getPassword()); // Remember to hash passwords in a real application!
//        instructor.setEmail(dto.getEmail());
//        instructor.setFirstName(dto.getFirstName());
//        instructor.setLastName(dto.getLastName());
//        instructor.setAuthorization(false); // Default to not authorized
//        instructor.setIsVerified(false);
//
//        Instructor savedInstructor = instructorRepository.save(instructor);
//        return userMapper.toInstructorDto(savedInstructor);
//    }
//
//    public InstructorDto getInstructorById(UUID id) {
//        Instructor instructor = instructorRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Instructor not found"));
//        return userMapper.toInstructorDto(instructor);
//    }
//
//    public List<InstructorDto> getAllInstructors() {
//        return instructorRepository.findAll().stream()
//                .map(userMapper::toInstructorDto)
//                .collect(Collectors.toList());
//    }
//
//    @Transactional
//    public InstructorDto updateInstructor(UUID id, InstructorCreationDto dto) {
//        Instructor instructor = instructorRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Instructor not found"));
//        if (!instructor.getUsername().equals(dto.getUsername())) {
//            userRepository.findByUsername(dto.getUsername()).ifPresent(u -> {
//                throw new DuplicateResourceException("Username already exists: " + dto.getUsername());
//            });
//        }
//        if (!instructor.getEmail().equals(dto.getEmail())) {
//            userRepository.findByEmail(dto.getEmail()).ifPresent(u -> {
//                throw new DuplicateResourceException("Email already exists: " + dto.getEmail());
//            });
//        }
//        instructor.setUsername(dto.getUsername());
//        instructor.setEmail(dto.getEmail());
//        instructor.setFirstName(dto.getFirstName());
//        instructor.setLastName(dto.getLastName());
//        // Note: Password updates should be handled separately and securely
//
//        Instructor updatedInstructor = instructorRepository.save(instructor);
//        return userMapper.toInstructorDto(updatedInstructor);
//    }
//
//    @Transactional
//    public InstructorResumeDto uploadResume(UUID instructorId, MultipartFile file) {
//        Instructor instructor = instructorRepository.findById(instructorId)
//                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found with id: " + instructorId));
//
//        String fileName = instructorId.toString() + "_" + file.getOriginalFilename();
//
//        try {
//            Path targetLocation = this.fileStorageLocation.resolve(fileName);
//            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//
//            InstructorResume resume = resumeRepository.findByInstructor(instructor).orElse(new InstructorResume());
//            resume.setInstructor(instructor);
//            resume.setResumeUrl("/uploads/resumes/" + fileName);
//            resume.setUploadedAt(LocalDateTime.now());
//
//            InstructorResume savedResume = resumeRepository.save(resume);
//            return userMapper.toInstructorResumeDto(savedResume);
//
//        } catch (IOException ex) {
//            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
//        }
//    }
//
//    public InstructorResumeDto getResumeByInstructorId(UUID instructorId) {
//        Instructor instructor = instructorRepository.findById(instructorId)
//                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found with id: " + instructorId));
//        InstructorResume resume = resumeRepository.findByInstructor(instructor)
//                .orElseThrow(() -> new ResourceNotFoundException("Resume not found for instructor with id: " + instructorId));
//        return userMapper.toInstructorResumeDto(resume);
//    }
//}

package com.tap.services;

import com.tap.dto.InstructorCreationDto;
import com.tap.dto.InstructorDto;
import com.tap.dto.InstructorResumeDto;
import com.tap.entities.Instructor;
import com.tap.entities.InstructorResume;
import com.tap.exceptions.ResourceNotFoundException;
import com.tap.exceptions.DuplicateResourceException;
import com.tap.repositories.UserRepository;
import com.tap.mappers.UserMapper;
import com.tap.repositories.InstructorRepository;
import com.tap.repositories.InstructorResumeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InstructorService {

    private final InstructorRepository instructorRepository;
    private final UserMapper userMapper;
    private final InstructorResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final Path fileStorageLocation;
    private final PasswordEncoder passwordEncoder;

    public InstructorService(InstructorRepository instructorRepository,
                             UserMapper userMapper,
                             InstructorResumeRepository resumeRepository,
                             UserRepository userRepository,
                             @Value("${file.upload-dir}") String uploadDir,
                             PasswordEncoder passwordEncoder) {
        this.instructorRepository = instructorRepository;
        this.userMapper = userMapper;
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Transactional
    public InstructorDto createInstructor(InstructorCreationDto dto) {
        if (dto.getPassword() == null || dto.getPassword().length() < 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must be at least 6 characters long");
        }
        userRepository.findByUsername(dto.getUsername()).ifPresent(u -> {
            throw new DuplicateResourceException("Username already exists: " + dto.getUsername());
        });
        userRepository.findByEmail(dto.getEmail()).ifPresent(u -> {
            throw new DuplicateResourceException("Email already exists: " + dto.getEmail());
        });
        Instructor instructor = new Instructor();
        instructor.setUsername(dto.getUsername());

        instructor.setPassword(passwordEncoder.encode(dto.getPassword()));

        instructor.setEmail(dto.getEmail());
        instructor.setFirstName(dto.getFirstName());
        instructor.setLastName(dto.getLastName());
    instructor.setAuthorization(false); // Default to not authorized
    instructor.setIsVerified(false); // Keep in sync

        Instructor savedInstructor = instructorRepository.save(instructor);
        return userMapper.toInstructorDto(savedInstructor);
    }

    public InstructorDto getInstructorById(UUID id) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));
        return userMapper.toInstructorDto(instructor);
    }

    public List<InstructorDto> getAllInstructors() {
        return instructorRepository.findAll().stream()
                .map(userMapper::toInstructorDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public InstructorDto updateInstructor(UUID id, InstructorCreationDto dto) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));

        if (!instructor.getUsername().equals(dto.getUsername())) {
            userRepository.findByUsername(dto.getUsername()).ifPresent(u -> {
                throw new DuplicateResourceException("Username already exists: " + dto.getUsername());
            });
        }
        if (!instructor.getEmail().equals(dto.getEmail())) {
            userRepository.findByEmail(dto.getEmail()).ifPresent(u -> {
                throw new DuplicateResourceException("Email already exists: " + dto.getEmail());
            });
        }
        // Optional password update with validation
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            if (dto.getPassword().length() < 6) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must be at least 6 characters long");
            }
            instructor.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        instructor.setUsername(dto.getUsername());
        instructor.setEmail(dto.getEmail());
        instructor.setFirstName(dto.getFirstName());
        instructor.setLastName(dto.getLastName());
        // Note: Password updates should be handled separately and securely

        Instructor updatedInstructor = instructorRepository.save(instructor);
        return userMapper.toInstructorDto(updatedInstructor);
    }

    @Transactional
    public InstructorResumeDto uploadResume(UUID instructorId, MultipartFile file) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found with id: " + instructorId));

        String fileName = instructorId.toString() + "_" + file.getOriginalFilename();

        try {
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            InstructorResume resume = resumeRepository.findByInstructor(instructor).orElse(new InstructorResume());
            resume.setInstructor(instructor);
            resume.setResumeUrl("/uploads/resumes/" + fileName);
            resume.setUploadedAt(LocalDateTime.now());

            InstructorResume savedResume = resumeRepository.save(resume);
            return userMapper.toInstructorResumeDto(savedResume);

        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public InstructorResumeDto getResumeByInstructorId(UUID instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found with id: " + instructorId));
        InstructorResume resume = resumeRepository.findByInstructor(instructor)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found for instructor with id: " + instructorId));
        return userMapper.toInstructorResumeDto(resume);
    }
}