//package com.tap.controllers;
//
//import com.tap.dto.*;
//import com.tap.services.StudentService;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import com.tap.entities.StudentBankDetails;
//import com.tap.dto.StudentBankDetailsDto;
//import com.tap.dto.StudentPaymentDto;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.List;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api/students")
//public class StudentController {
//
//    private final StudentService studentService;
//
//    public StudentController(StudentService studentService) {
//        this.studentService = studentService;
//    }
//
//    @PostMapping
//    public ResponseEntity<?> createStudent(@RequestBody StudentCreationDto studentDto) {
//        try {
//            return new ResponseEntity<>(studentService.createStudent(studentDto), HttpStatus.CREATED);
//        } catch (ResponseStatusException ex) {
//            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
//        } catch (Exception e) {
//            return new ResponseEntity<>("Failed to create student", HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<StudentDto> getStudentById(@PathVariable UUID id) {
//        return ResponseEntity.ok(studentService.getStudentById(id));
//    }
//
//    @GetMapping
//    public ResponseEntity<List<StudentDto>> getAllStudents() {
//        return ResponseEntity.ok(studentService.getAllStudents());
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<?> updateStudent(@PathVariable UUID id, @RequestBody StudentCreationDto studentDto) {
//        try {
//            return ResponseEntity.ok(studentService.updateStudent(id, studentDto));
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    @PostMapping("/{id}/preferences")
//    public ResponseEntity<?> createOrUpdatePreferences(@PathVariable UUID id, @RequestBody StudentPreferenceDto preferenceDto) {
//        try {
//            return new ResponseEntity<>(studentService.createOrUpdatePreference(id, preferenceDto), HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    @GetMapping("/{id}/preferences")
//    public ResponseEntity<?> getPreferences(@PathVariable UUID id) {
//        try {
//            return ResponseEntity.ok(studentService.getPreferenceByStudentId(id));
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//        }
//    }
//
//    @PostMapping("/{studentId}/bankDetails")
//    public ResponseEntity<StudentBankDetailsDto> createOrUpdateBankDetail(@PathVariable UUID studentId, @RequestBody StudentBankDetailsDto bankDetailsDto){
//        StudentBankDetailsDto savedDetails = studentService.createOrUpdateBankDetail(studentId,bankDetailsDto);
//        return ResponseEntity.ok(savedDetails);
//    }
//
//    @GetMapping("/{studentId}/bankDetails")
//    public ResponseEntity<StudentBankDetailsDto> getBankDetailsByStudentId(@PathVariable UUID studentId){
//        StudentBankDetailsDto details = studentService.getBankDetailsByStudentId(studentId);
//        return ResponseEntity.ok(details);
//    }
//
//    @DeleteMapping("/{studentId}/bankDetails")
//    public ResponseEntity<String> deleteBankDetailsByID(@PathVariable UUID studentId){
//        studentService.deleteBankDetailsByID(studentId);
//        return ResponseEntity.ok("Bank details deleted successfully");
//    }
//
//    @PostMapping("/{studentId}/payments")
//    public ResponseEntity<?> createPayment(@PathVariable UUID studentId, @RequestBody StudentPaymentDto paymentDto){
//        try {
//            paymentDto.setStudent(studentId);
//            StudentPaymentDto createdPayment = studentService.createPayment(studentId,paymentDto);
//            return new ResponseEntity<>(createdPayment, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    @GetMapping("/payments/{paymentId}") // this endpoint does not seem good
//    public ResponseEntity<?> getPaymentById(@PathVariable Integer paymentId){
//        try {
//            StudentPaymentDto payment = studentService.getPaymentById(paymentId);
//            return ResponseEntity.ok(payment);
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
//        }
//
//    }
//
//}

package com.tap.controllers;

import com.tap.dto.*;
import com.tap.services.CustomUserDetails;
import com.tap.services.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> createStudent(@RequestBody StudentCreationDto studentDto) {
        try {
            return new ResponseEntity<>(studentService.createStudent(studentDto), HttpStatus.CREATED);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create student", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getLoggedInStudent(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isStudent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        UUID studentId = userDetails.getUser().getUserId();
        return ResponseEntity.ok(studentService.getStudentById(studentId));
    }

//    to get list of instructors
@GetMapping("/instructors")
public ResponseEntity<List<InstructorDto>> getAllInstructorsForStudents(Authentication authentication) {
    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
    if (!userDetails.isStudent()) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }
    return ResponseEntity.ok(studentService.getAllInstructorsForStudents());
}

    @PutMapping("/me")
    public ResponseEntity<?> updateLoggedInStudent(@RequestBody StudentCreationDto studentDto, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isStudent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        UUID studentId = userDetails.getUser().getUserId();
        try {
            return ResponseEntity.ok(studentService.updateStudent(studentId, studentDto));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/me/preferences")
    public ResponseEntity<?> createOrUpdatePreferences(@RequestBody StudentPreferenceDto preferenceDto, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isStudent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        UUID studentId = userDetails.getUser().getUserId();
        try {
            return new ResponseEntity<>(studentService.createOrUpdatePreference(studentId, preferenceDto), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/me/preferences")
    public ResponseEntity<?> getPreferences(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isStudent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        UUID studentId = userDetails.getUser().getUserId();
        try {
            return ResponseEntity.ok(studentService.getPreferenceByStudentId(studentId));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/me/bankDetails")
    public ResponseEntity<StudentBankDetailsDto> createOrUpdateBankDetail(@RequestBody StudentBankDetailsDto bankDetailsDto, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isStudent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        UUID studentId = userDetails.getUser().getUserId();
        StudentBankDetailsDto savedDetails = studentService.createOrUpdateBankDetail(studentId, bankDetailsDto);
        return ResponseEntity.ok(savedDetails);
    }

    @GetMapping("/me/bankDetails")
    public ResponseEntity<StudentBankDetailsDto> getBankDetailsByStudentId(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isStudent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        UUID studentId = userDetails.getUser().getUserId();
        StudentBankDetailsDto details = studentService.getBankDetailsByStudentId(studentId);
        return ResponseEntity.ok(details);
    }

    @DeleteMapping("/me/bankDetails")
    public ResponseEntity<String> deleteBankDetailsByID(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isStudent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        UUID studentId = userDetails.getUser().getUserId();
        studentService.deleteBankDetailsByID(studentId);
        return ResponseEntity.ok("Bank details deleted successfully");
    }

    @PostMapping("/me/payments")
    public ResponseEntity<?> createPayment(@RequestBody StudentPaymentDto paymentDto, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isStudent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        UUID studentId = userDetails.getUser().getUserId();
        try {
            paymentDto.setStudent(studentId);
            StudentPaymentDto createdPayment = studentService.createPayment(studentId, paymentDto);
            return new ResponseEntity<>(createdPayment, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/me/payments")
    public ResponseEntity<?> listMyPayments(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isStudent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        UUID studentId = userDetails.getUser().getUserId();
        return ResponseEntity.ok(studentService.getPaymentsByStudent(studentId));
    }

    @GetMapping("/me/payments/{paymentId}")
    public ResponseEntity<?> getMyPaymentById(@PathVariable Integer paymentId, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isStudent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        UUID studentId = userDetails.getUser().getUserId();
        try {
            StudentPaymentDto payment = studentService.getPaymentById(paymentId);
            if (payment == null || payment.getStudent() == null || !payment.getStudent().equals(studentId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not your payment");
            }
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<StudentDto>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<StudentDto> getStudentByIdAdmin(@PathVariable UUID id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<?> updateStudentById(@PathVariable UUID id, @RequestBody StudentCreationDto studentDto) {
        try {
            return ResponseEntity.ok(studentService.updateStudent(id, studentDto));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}