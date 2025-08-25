package com.tap.controllers;

import com.tap.dto.CourseCreationDto;
import com.tap.dto.CourseDto;
import com.tap.services.CourseService;
import com.tap.services.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // Instructor creates a course (instructorId overridden by auth principal)
    @PostMapping("/instructors/me")
    public ResponseEntity<?> createCourseForInstructor(@RequestBody CourseCreationDto courseDto, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isInstructor()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        // Rebuild DTO with authenticated instructor id
        courseDto = new CourseCreationDto(
                userDetails.getUser().getUserId(),
                courseDto.title(),
                courseDto.description(),
                courseDto.skillId(),
                courseDto.price(),
                courseDto.duration(),
                courseDto.levelId(),
                courseDto.isPublished()
        );
        try {
            return new ResponseEntity<>(courseService.createCourse(courseDto), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Student fetch single course (published only) or instructor fetch own regardless
    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable UUID id, Authentication authentication) {
        try {
            CourseDto dto = courseService.getCourseById(id);
            if (authentication != null) {
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                if (userDetails.isInstructor() && dto.instructorId().equals(userDetails.getUser().getUserId())) {
                    return ResponseEntity.ok(dto); // owner instructor
                }
            }
            if (!dto.isPublished()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Course not published");
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Student list published courses
    @GetMapping("/students/me")
    public ResponseEntity<?> getPublishedCoursesForStudent(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isStudent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        return ResponseEntity.ok(courseService.getPublishedCourses());
    }

    // Instructor list their own courses
    @GetMapping("/instructors/me")
    public ResponseEntity<?> getCoursesForInstructor(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isInstructor()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        return ResponseEntity.ok(courseService.getCoursesByInstructor(userDetails.getUser().getUserId()));
    }

    @PutMapping("/instructors/me/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable UUID id, @RequestBody CourseCreationDto courseDto, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isInstructor()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        // enforce instructor ownership by overriding instructorId
        courseDto = new CourseCreationDto(
                userDetails.getUser().getUserId(),
                courseDto.title(),
                courseDto.description(),
                courseDto.skillId(),
                courseDto.price(),
                courseDto.duration(),
                courseDto.levelId(),
                courseDto.isPublished()
        );
        try {
            return ResponseEntity.ok(courseService.updateCourse(id, courseDto));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/instructors/me/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable UUID id, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isInstructor()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        // Ownership check inside service (will add) or here after fetch
        CourseDto existing = courseService.getCourseById(id);
        if (!existing.instructorId().equals(userDetails.getUser().getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not owner of course");
        }
        courseService.deleteCourse(id);
    return ResponseEntity.ok(java.util.Map.of(
        "message", "Course deleted successfully",
        "courseId", id.toString()
    ));
    }
}
