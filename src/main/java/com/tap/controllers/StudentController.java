package com.tap.controllers;

import com.tap.dto.*;
import com.tap.services.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tap.entities.StudentBankDetails;
import com.tap.dto.StudentBankDetailsDto;
import com.tap.dto.StudentPaymentDto;
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

    @PostMapping
    public ResponseEntity<?> createStudent(@RequestBody StudentCreationDto studentDto) {
        try {
            return new ResponseEntity<>(studentService.createStudent(studentDto), HttpStatus.CREATED);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create student", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable UUID id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @GetMapping
    public ResponseEntity<List<StudentDto>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable UUID id, @RequestBody StudentCreationDto studentDto) {
        try {
            return ResponseEntity.ok(studentService.updateStudent(id, studentDto));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}/preferences")
    public ResponseEntity<?> createOrUpdatePreferences(@PathVariable UUID id, @RequestBody StudentPreferenceDto preferenceDto) {
        try {
            return new ResponseEntity<>(studentService.createOrUpdatePreference(id, preferenceDto), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}/preferences")
    public ResponseEntity<?> getPreferences(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(studentService.getPreferenceByStudentId(id));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{studentId}/bankDetails")
    public ResponseEntity<StudentBankDetailsDto> createOrUpdateBankDetail(@PathVariable UUID studentId, @RequestBody StudentBankDetailsDto bankDetailsDto){
        StudentBankDetailsDto savedDetails = studentService.createOrUpdateBankDetail(studentId,bankDetailsDto);
        return ResponseEntity.ok(savedDetails);
    }

    @GetMapping("/{studentId}/bankDetails")
    public ResponseEntity<StudentBankDetailsDto> getBankDetailsByStudentId(@PathVariable UUID studentId){
        StudentBankDetailsDto details = studentService.getBankDetailsByStudentId(studentId);
        return ResponseEntity.ok(details);
    }

    @DeleteMapping("/{studentId}/bankDetails")
    public ResponseEntity<String> deleteBankDetailsByID(@PathVariable UUID studentId){
        studentService.deleteBankDetailsByID(studentId);
        return ResponseEntity.ok("Bank details deleted successfully");
    }

    @PostMapping("/{studentId}/payments")
    public ResponseEntity<?> createPayment(@PathVariable UUID studentId, @RequestBody StudentPaymentDto paymentDto){
        try {
            paymentDto.setStudent(studentId);
            StudentPaymentDto createdPayment = studentService.createPayment(studentId,paymentDto);
            return new ResponseEntity<>(createdPayment, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/payments/{paymentId}") // this endpoint does not seem good
    public ResponseEntity<?> getPaymentById(@PathVariable Integer paymentId){
        try {
            StudentPaymentDto payment = studentService.getPaymentById(paymentId);
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }

    }

}
