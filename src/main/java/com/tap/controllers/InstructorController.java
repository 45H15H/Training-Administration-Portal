//package com.tap.controllers;
//
//import com.tap.dto.InstructorCreationDto;
//import com.tap.dto.InstructorDto;
//import com.tap.dto.InstructorResumeDto;
//import com.tap.services.InstructorService;
//import com.tap.exceptions.DuplicateResourceException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.List;
//import java.util.UUID;
//
//@RestController
//    @RequestMapping("/api/instructors")
//public class InstructorController {
//
//    private final InstructorService instructorService;
//
//    public InstructorController(InstructorService instructorService) {
//        this.instructorService = instructorService;
//    }
//
//    @PostMapping
//    public ResponseEntity<?> createInstructor(@RequestBody InstructorCreationDto instructorDto) {
//        try {
//            return new ResponseEntity<>(instructorService.createInstructor(instructorDto), HttpStatus.CREATED);
//        } catch (DuplicateResourceException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<InstructorDto> getInstructorById(@PathVariable UUID id) {
//        return ResponseEntity.ok(instructorService.getInstructorById(id));
//    }
//
//    @GetMapping
//    public ResponseEntity<List<InstructorDto>> getAllInstructors() {
//        return ResponseEntity.ok(instructorService.getAllInstructors());
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<?> updateInstructor(@PathVariable UUID id, @RequestBody InstructorCreationDto instructorDto) {
//        try {
//            return ResponseEntity.ok(instructorService.updateInstructor(id, instructorDto));
//        } catch (DuplicateResourceException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    @PostMapping("/{id}/resume")
//    public ResponseEntity<?> uploadResume(@PathVariable UUID id, @RequestParam("file") MultipartFile file) {
//        try {
//            InstructorResumeDto resumeDto = instructorService.uploadResume(id, file);
//            return new ResponseEntity<>(resumeDto, HttpStatus.CREATED);
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    @GetMapping("/{id}/resume")
//    public ResponseEntity<InstructorResumeDto> getResume(@PathVariable UUID id) {
//        return ResponseEntity.ok(instructorService.getResumeByInstructorId(id));
//    }
//
//    @PutMapping("/{id}/resume")
//    public ResponseEntity<?> updateResume(@PathVariable UUID id, @RequestParam("file") MultipartFile file) {
//        try {
//            InstructorResumeDto resumeDto = instructorService.uploadResume(id, file);
//            return ResponseEntity.ok(resumeDto);
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }
//}


//muskan code
package com.tap.controllers;

import com.tap.dto.InstructorCreationDto;
import com.tap.dto.InstructorDto;
import com.tap.dto.InstructorResumeDto;
import com.tap.entities.UserPrincipal;
import com.tap.exceptions.DuplicateResourceException;
import com.tap.services.CustomUserDetails;
import com.tap.services.InstructorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/instructors")
public class InstructorController {


    private final InstructorService instructorService;

    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> createInstructor(@RequestBody InstructorCreationDto instructorDto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(instructorService.createInstructor(instructorDto));
        } catch (DuplicateResourceException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getLoggedInInstructor(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isInstructor()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        UUID instructorId = userDetails.getUser().getUserId();
        return ResponseEntity.ok(instructorService.getInstructorById(instructorId));
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateLoggedInInstructor(@RequestBody InstructorCreationDto instructorDto, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isInstructor()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        UUID instructorId = userDetails.getUser().getUserId();
        try {
            return ResponseEntity.ok(instructorService.updateInstructor(instructorId, instructorDto));
        } catch (DuplicateResourceException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/me/resume")
    public ResponseEntity<?> uploadResume(@RequestParam("file") MultipartFile file, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isInstructor()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        UUID instructorId = userDetails.getUser().getUserId();
        try {
            InstructorResumeDto resumeDto = instructorService.uploadResume(instructorId, file);
            return ResponseEntity.status(HttpStatus.CREATED).body(resumeDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/me/resume")
    public ResponseEntity<?> getResume(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isInstructor()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        UUID instructorId = userDetails.getUser().getUserId();
        return ResponseEntity.ok(instructorService.getResumeByInstructorId(instructorId));
    }

    @PutMapping("/me/resume")
    public ResponseEntity<?> updateResume(@RequestParam("file") MultipartFile file, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isInstructor()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        UUID instructorId = userDetails.getUser().getUserId();
        try {
            InstructorResumeDto resumeDto = instructorService.uploadResume(instructorId, file);
            return ResponseEntity.ok(resumeDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 🔐 Admin-only access
    @GetMapping("/admin/{id}")
    public ResponseEntity<InstructorDto> getInstructorByIdAdmin(@PathVariable UUID id) {
        return ResponseEntity.ok(instructorService.getInstructorById(id));
    }

    @GetMapping
    public ResponseEntity<List<InstructorDto>> getAllInstructors() {
        return ResponseEntity.ok(instructorService.getAllInstructors());
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<?> updateInstructorById(@PathVariable UUID id, @RequestBody InstructorCreationDto instructorDto) {
        try {
            return ResponseEntity.ok(instructorService.updateInstructor(id, instructorDto));
        } catch (DuplicateResourceException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/admin/{id}/resume")
    public ResponseEntity<?> uploadResumeById(@PathVariable UUID id, @RequestParam("file") MultipartFile file) {
        try {
            InstructorResumeDto resumeDto = instructorService.uploadResume(id, file);
            return ResponseEntity.status(HttpStatus.CREATED).body(resumeDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/admin/{id}/resume")
    public ResponseEntity<InstructorResumeDto> getResumeById(@PathVariable UUID id) {
        return ResponseEntity.ok(instructorService.getResumeByInstructorId(id));
    }

    @PutMapping("/admin/{id}/resume")
    public ResponseEntity<?> updateResumeById(@PathVariable UUID id, @RequestParam("file") MultipartFile file) {
        try {
            InstructorResumeDto resumeDto = instructorService.uploadResume(id, file);
            return ResponseEntity.ok(resumeDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
