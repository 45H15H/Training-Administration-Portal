package com.tap.services;

import com.tap.dto.BookingRequestDto;
import com.tap.dto.StudentBookingDto;
import com.tap.entities.*;
import com.tap.exceptions.ResourceNotFoundException;
import com.tap.mappers.UserMapper;
import com.tap.repositories.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class StudentBookingService {

    private final StudentBookingRepository bookingRepository;
    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;
    private final InstructorTimeSlotRepository timeSlotRepository;
    private final UserMapper userMapper;

    private final CourseRepository courseRepository;
    private final StudentCourseEnrollmentRepository enrollmentRepository;

    public StudentBookingService(StudentBookingRepository bookingRepository, StudentRepository studentRepository,
                                 InstructorRepository instructorRepository, InstructorTimeSlotRepository timeSlotRepository,
                                 UserMapper userMapper, CourseRepository courseRepository,
                                 StudentCourseEnrollmentRepository enrollmentRepository) {
        this.bookingRepository = bookingRepository;
        this.studentRepository = studentRepository;
        this.instructorRepository = instructorRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.userMapper = userMapper;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Transactional
    public StudentBookingDto createBooking(java.util.UUID studentId, BookingRequestDto bookingRequest) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        Instructor instructor = instructorRepository.findById(bookingRequest.instructorId())
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));
    InstructorTimeSlot timeSlot = timeSlotRepository.findById(bookingRequest.timeSlotId())
                .orElseThrow(() -> new ResourceNotFoundException("Time slot not found"));

        // Fetch and validate course
    Course course = courseRepository.findById(bookingRequest.courseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        // Reject if course not published
        if (!Boolean.TRUE.equals(course.getIsPublished())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course is not published");
        }

        // Ensure course belongs to the specified instructor
        if (course.getInstructor() == null || !course.getInstructor().getUserId().equals(instructor.getUserId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Selected course does not belong to the specified instructor");
        }

        // Ensure time slot belongs to the specified instructor
        if (timeSlot.getInstructor() == null || !timeSlot.getInstructor().getUserId().equals(instructor.getUserId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Selected time slot does not belong to the specified instructor");
        }

        // Ensure course and time slot belong to the same instructor
        if (!course.getInstructor().getUserId().equals(timeSlot.getInstructor().getUserId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Selected course and time slot belong to different instructors");
        }

        if (timeSlot.getIsBooked()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Time slot is not available");
        }

        StudentBooking booking = new StudentBooking();
        booking.setStudent(student);
        booking.setInstructor(instructor);
        booking.setSlot(timeSlot);
        booking.setStatus("CONFIRMED");

        timeSlot.setIsBooked(true);
        timeSlotRepository.save(timeSlot);

        StudentBooking savedBooking = bookingRepository.save(booking);

        // Auto-enroll student if not already enrolled in course
        boolean enrolled = enrollmentRepository.existsByStudent_UserIdAndCourse_CourseId(student.getUserId(), course.getCourseId());
        if (!enrolled) {
            StudentCourseEnrollment enrollment = new StudentCourseEnrollment();
            enrollment.setStudent(student);
            enrollment.setCourse(course);
            enrollment.setStatus("ENROLLED");
            enrollment.setProgress(java.math.BigDecimal.ZERO);
            enrollmentRepository.save(enrollment);
        }
        return userMapper.toStudentBookingDto(savedBooking);
    }
}
