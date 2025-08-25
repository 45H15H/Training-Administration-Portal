//package com.tap.services;
//
//import com.tap.dto.*;
//import com.tap.entities.*;
//import com.tap.exceptions.ResourceNotFoundException;
//import com.tap.mappers.UserMapper;
//import com.tap.repositories.*;
//import jakarta.transaction.Transactional;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//@Service
//public class StudentService {
//
//    private final StudentRepository studentRepository;
//    private final UserMapper userMapper;
//    private final StudentPreferenceRepository preferenceRepository;
//    private final StudentBankDetailsRepository bankDetailsRepository;
//    private final StudentPaymentRepository paymentRepository;
//    private final CourseRepository courseRepository;
//    private final InstructorEarningRepository instructorEarningRepository;
//    private final InstructorBankDetailRepository instructorBankDetailRepository;
//    private final StudentCourseEnrollmentRepository enrollmentRepository;
//
//    public StudentService(StudentRepository studentRepository, UserMapper userMapper, StudentPreferenceRepository preferenceRepository, StudentBankDetailsRepository bankDetailsRepository, StudentPaymentRepository paymentRepository, CourseRepository courseRepository, InstructorEarningRepository instructorEarningRepository, InstructorBankDetailRepository instructorBankDetailRepository, StudentCourseEnrollmentRepository enrollmentRepository) {
//        this.studentRepository = studentRepository;
//        this.userMapper = userMapper;
//        this.preferenceRepository = preferenceRepository;
//        this.bankDetailsRepository = bankDetailsRepository;
//        this.paymentRepository = paymentRepository;
//        this.courseRepository = courseRepository;
//        this.instructorEarningRepository = instructorEarningRepository;
//        this.instructorBankDetailRepository = instructorBankDetailRepository;
//        this.enrollmentRepository = enrollmentRepository;
//    }
//
//    @Transactional
//    public StudentDto createStudent(StudentCreationDto dto) {
//        // Pre-check to provide clear conflict messages
//        if (studentRepository.existsByUsername(dto.getUsername())) {
//            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already in use");
//        }
//        if (studentRepository.existsByEmail(dto.getEmail())) {
//            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already in use");
//        }
//        if (studentRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
//            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone number is already in use");
//        }
//
//        try {
//            Student student = new Student();
//            student.setUsername(dto.getUsername());
//            student.setPassword(dto.getPassword()); // Remember to hash passwords!
//            student.setEmail(dto.getEmail());
//            student.setFirstName(dto.getFirstName());
//            student.setLastName(dto.getLastName());
//            student.setAge(dto.getAge());
//            student.setPhoneNumber(dto.getPhoneNumber());
//            student.setAuthorization(false);
//            student.setIsVerified(false);
//
//            Student savedStudent = studentRepository.save(student);
//            return userMapper.toStudentDto(savedStudent);
//        } catch (DataIntegrityViolationException ex) {
//            // Fallback in case of a race condition causing a DB unique constraint violation
//            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username, email, or phone number already exists");
//        }
//    }
//
//    public StudentDto getStudentById(UUID id) {
//        Student student = studentRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Student not found"));
//        return userMapper.toStudentDto(student);
//    }
//
//    public List<StudentDto> getAllStudents() {
//        return studentRepository.findAll().stream()
//                .map(userMapper::toStudentDto)
//                .collect(Collectors.toList());
//    }
//
//    @Transactional
//    public StudentDto updateStudent(UUID id, StudentCreationDto dto) {
//        Student student = studentRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Student not found"));
//
//        student.setUsername(dto.getUsername());
//        student.setEmail(dto.getEmail());
//        student.setFirstName(dto.getFirstName());
//        student.setLastName(dto.getLastName());
//        student.setPhoneNumber(dto.getPhoneNumber());
//        student.setAge(dto.getAge());
//
//        Student updatedStudent = studentRepository.save(student);
//        return userMapper.toStudentDto(updatedStudent);
//    }
//
//    @Transactional
//    public StudentPreferenceDto createOrUpdatePreference(UUID studentId, StudentPreferenceDto dto) {
//        Student student = studentRepository.findById(studentId)
//                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
//
//        StudentPreference preference = preferenceRepository.findByStudent(student)
//                .orElse(new StudentPreference());
//
//        preference.setStudent(student);
//        preference.setRequiredSkills(dto.getRequiredSkills());
//        preference.setPreferredTimeSlots(dto.getPreferredTimeSlots());
//        preference.setPreferredStartTime(dto.getPreferredStartTime());
//        preference.setPreferredEndTime(dto.getPreferredEndTime());
//
//        StudentPreference savedPreference = preferenceRepository.save(preference);
//        return userMapper.toStudentPreferenceDto(savedPreference);
//    }
//
//    public StudentPreferenceDto getPreferenceByStudentId(UUID studentId) {
//        Student student = studentRepository.findById(studentId)
//                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
//        StudentPreference preference = preferenceRepository.findByStudent(student)
//                .orElseThrow(() -> new ResourceNotFoundException("Preferences not found for student with id: " + studentId));
//        return userMapper.toStudentPreferenceDto(preference);
//    }
//
//    //BankDetails operations
//    @Transactional
//    public StudentBankDetailsDto createOrUpdateBankDetail(UUID studentId, StudentBankDetailsDto dto){
//        Student student = studentRepository.findById(studentId)
//                .orElseThrow(()-> new ResourceNotFoundException("Student not found with id :"));
//
//        if(dto.getAccountNumber()== null || dto.getAccountNumber().isEmpty()){
//            throw new IllegalArgumentException("Account Number is required");
//        }
//
//        if(dto.getIfscCode()==null || dto.getIfscCode().isEmpty()){
//            throw new IllegalArgumentException("IFSC code is required");
//        }
//        List<StudentBankDetails> existingDetails = bankDetailsRepository.findByStudent_UserId(studentId);
//
//        //checks for duplicate account number
//        boolean exists = bankDetailsRepository.existsByAccountNumber(dto.getAccountNumber());
//        if(exists && (existingDetails.isEmpty() || !existingDetails.get(0).getAccountNumber().equals(dto.getAccountNumber()))){
//            throw  new IllegalArgumentException("Account Number already exists !!");
//        }
//
//        StudentBankDetails studentBankDetails;
//        if(!existingDetails.isEmpty()){
//            studentBankDetails = existingDetails.get(0);
//        }else {
//            studentBankDetails = new StudentBankDetails();
//            studentBankDetails.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : java.time.LocalDateTime.now());
//        }
//        studentBankDetails.setStudent(student);
//        studentBankDetails.setAccountHolderName(dto.getAccountHolderName());
//        studentBankDetails.setBankName(dto.getBankName());
//        studentBankDetails.setAccountNumber(dto.getAccountNumber());
//        studentBankDetails.setIfscCode(dto.getIfscCode());
//        studentBankDetails.setAccountType(dto.getAccountType());
//        studentBankDetails.setBranchName(dto.getBankName());
//
//        StudentBankDetails savedBankDetails = bankDetailsRepository.save(studentBankDetails);
//        return userMapper.tostudentBankDetailsDto(savedBankDetails);
//    }
//
//    public StudentBankDetailsDto getBankDetailsByStudentId(UUID studentId){
//        Student student = studentRepository.findById(studentId)
//                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: "));
//        StudentBankDetails studentBankDetails = bankDetailsRepository.findByStudent_UserId(studentId)
//                .stream()
//                .findFirst()
//                .orElseThrow(()->new ResourceNotFoundException("Bank details not found for student with id:"+studentId));
//
//        return userMapper.tostudentBankDetailsDto(studentBankDetails);
//    }
//
//    @Transactional
//    public void deleteBankDetailsByID(UUID studentId){
//        Student student = studentRepository.findById(studentId)
//                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id :"));
//        List<StudentBankDetails> bankDetailsList = bankDetailsRepository.findByStudent_UserId(studentId);
//
//        if (bankDetailsList.isEmpty()){
//            throw  new ResourceNotFoundException("No bank details found for student with id :");
//        }
//        bankDetailsRepository.deleteAll(bankDetailsList);
//    }
//
//    /*
//    public StudentBookingResponseDto createBooking(StudentBookingRequestDto studentBookingRequestDto){
//        Student student = studentRepository.findById(studentBookingRequestDto.getStudent())
//                .orElseThrow(() ->new ResourceNotFoundException("Student not found with id:"+studentBookingRequestDto.getStudent()));
//
//        Instructor instructor = instructorRepository.findById(studentBookingRequestDto.getInstructor())
//                .orElseThrow(()->new ResourceNotFoundException("Instructor not found with id:"+studentBookingRequestDto.getInstructor()));
//
//
//    }*/
//
//    @Transactional
//    public StudentPaymentDto createPayment(UUID studentId, StudentPaymentDto dto){
//        Student student = studentRepository.findById(dto.getStudent())
//                .orElseThrow(() ->new ResourceNotFoundException("Student not found with id:"+dto.getStudent()));
//        StudentBankDetails bankDetails = null;
//        if(dto.getStudentBankDetails() != null){
//            bankDetails = bankDetailsRepository.findById(dto.getStudentBankDetails())
//                    .orElseThrow(()->new ResourceNotFoundException("Bank details not found with id:"+dto.getStudentBankDetails()));
//        }
//
//        // Fetch course & instructor
//        Course course = null;
//        Instructor instructor = null;
//        if(dto.getCourseId() != null){
//            course = courseRepository.findById(dto.getCourseId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Course not found with id:"+dto.getCourseId()));
//            instructor = course.getInstructor();
//        } else {
//            throw new IllegalArgumentException("courseId is required for payment");
//        }
//
//        // Ensure student enrolled in course before payment
//        boolean enrolled = enrollmentRepository.existsByStudent_UserIdAndCourse_CourseId(student.getUserId(), course.getCourseId());
//        if (!enrolled) {
//            throw new IllegalStateException("Student must be enrolled in course before payment");
//        }
//
//        StudentPayment payment = new StudentPayment();
//        payment.setStudent(student);
//        payment.setStudentBankDetails(bankDetails);
//        payment.setCourse(course);
//        payment.setAmount(dto.getAmount());
//        payment.setPaymentMethod(dto.getPaymentMethod());
//        payment.setPaymentStatus(dto.getPaymentStatus() != null ? dto.getPaymentStatus() : "COMPLETED");
//        payment.setPaidAt(dto.getPaidAt() !=null ? dto.getPaidAt() : LocalDateTime.now());
//        payment.setNotes(dto.getNotes());
//
//        StudentPayment savedPayment = paymentRepository.save(payment);
//
//        // Auto create instructor earning
//        if(instructor != null){
//            InstructorEarning earning = new InstructorEarning();
//            earning.setInstructor(instructor);
//            earning.setAmount(savedPayment.getAmount());
//            earning.setStudent(student);
//            earning.setPaymentMethod(savedPayment.getPaymentMethod());
//            // choose first bank detail of instructor if exists
//            instructorBankDetailRepository.findByInstructorUserId(instructor.getUserId())
//                    .stream().findFirst().ifPresent(earning::setBankDetail);
//            earning.setNotes("Auto-generated from student payment #"+ savedPayment.getPaymentId());
//            earning.setReceivedAt(LocalDateTime.now());
//            instructorEarningRepository.save(earning);
//        }
//
//        return userMapper.tostudentPaymentDto(savedPayment);
//    }
//
//
//    public StudentPaymentDto getPaymentById(Integer paymentId){
//        StudentPayment payment = paymentRepository.findById(paymentId)
//                .orElseThrow(()->new ResourceNotFoundException("Payment not found with id: "));
//        return userMapper.tostudentPaymentDto(payment);
//    }
//}


package com.tap.services;

import com.tap.dto.*;
import com.tap.entities.*;
import com.tap.exceptions.ResourceNotFoundException;
import com.tap.mappers.UserMapper;
import com.tap.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserMapper userMapper;
    private final StudentPreferenceRepository preferenceRepository;
    private final StudentBankDetailsRepository bankDetailsRepository;
    private final StudentPaymentRepository paymentRepository;
    private final CourseRepository courseRepository;
    private final InstructorEarningRepository instructorEarningRepository;
    private final InstructorBankDetailRepository instructorBankDetailRepository;
    private final StudentCourseEnrollmentRepository enrollmentRepository;
    private final PasswordEncoder passwordEncoder;

//    injecting instructorservice into studentservice
    private final InstructorService instructorService;

    public List<InstructorDto> getAllInstructorsForStudents(){
        return instructorService.getAllInstructors();
    }

    public StudentService(
            StudentRepository studentRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder,
            StudentPreferenceRepository preferenceRepository,
            StudentBankDetailsRepository bankDetailsRepository,
            StudentPaymentRepository paymentRepository,
            CourseRepository courseRepository,
            InstructorEarningRepository instructorEarningRepository,
            InstructorBankDetailRepository instructorBankDetailRepository,
            StudentCourseEnrollmentRepository enrollmentRepository, InstructorService instructorService
    ) {
        this.studentRepository = studentRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.preferenceRepository = preferenceRepository;
        this.bankDetailsRepository = bankDetailsRepository;
        this.paymentRepository = paymentRepository;
        this.courseRepository = courseRepository;
        this.instructorEarningRepository = instructorEarningRepository;
        this.instructorBankDetailRepository = instructorBankDetailRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.instructorService=instructorService;
    }


    @Transactional
    public StudentDto createStudent(StudentCreationDto dto) {
        // Pre-check to provide clear conflict messages
        if (studentRepository.existsByUsername(dto.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already in use");
        }
        if (studentRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already in use");
        }
        if (studentRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone number is already in use");
        }
        if (dto.getPassword() == null || dto.getPassword().length() < 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must be at least 6 characters long");
        }
        if (dto.getAge() != null && dto.getAge() < 15) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Age must be at least 15");
        }

        try {
            Student student = new Student();
            student.setUsername(dto.getUsername());


            String hashedPassword = passwordEncoder.encode(dto.getPassword());
            student.setPassword(hashedPassword);

            student.setEmail(dto.getEmail());
            student.setFirstName(dto.getFirstName());
            student.setLastName(dto.getLastName());
            student.setAge(dto.getAge());
            student.setPhoneNumber(dto.getPhoneNumber());
            // Students default to authorized
            student.setAuthorization(true);
            // Keep verification aligned with authorization for students at creation
            student.setIsVerified(true);

            Student savedStudent = studentRepository.save(student);
            return userMapper.toStudentDto(savedStudent);
        } catch (DataIntegrityViolationException ex) {
            // Fallback in case of a race condition causing a DB unique constraint violation
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username, email, or phone number already exists");
        }
    }

    public StudentDto getStudentById(UUID id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return userMapper.toStudentDto(student);
    }

    public List<StudentDto> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(userMapper::toStudentDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public StudentDto updateStudent(UUID id, StudentCreationDto dto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (dto.getPassword() != null && dto.getPassword().length() < 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must be at least 6 characters long");
        }
        if (dto.getAge() != null && dto.getAge() < 15) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Age must be at least 15");
        }

        student.setUsername(dto.getUsername());
        student.setEmail(dto.getEmail());
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setPhoneNumber(dto.getPhoneNumber());
        student.setAge(dto.getAge());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            student.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        Student updatedStudent = studentRepository.save(student);
        return userMapper.toStudentDto(updatedStudent);
    }

    @Transactional
    public StudentPreferenceDto createOrUpdatePreference(UUID studentId, StudentPreferenceDto dto) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        StudentPreference preference = preferenceRepository.findByStudent(student)
                .orElse(new StudentPreference());

        preference.setStudent(student);
        preference.setRequiredSkills(dto.getRequiredSkills());
        preference.setPreferredTimeSlots(dto.getPreferredTimeSlots());
        preference.setPreferredStartTime(dto.getPreferredStartTime());
        preference.setPreferredEndTime(dto.getPreferredEndTime());

        StudentPreference savedPreference = preferenceRepository.save(preference);
        return userMapper.toStudentPreferenceDto(savedPreference);
    }

    public StudentPreferenceDto getPreferenceByStudentId(UUID studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
        StudentPreference preference = preferenceRepository.findByStudent(student)
                .orElseThrow(() -> new ResourceNotFoundException("Preferences not found for student with id: " + studentId));
        return userMapper.toStudentPreferenceDto(preference);
    }

    //BankDetails operations
    @Transactional
    public StudentBankDetailsDto createOrUpdateBankDetail(UUID studentId, StudentBankDetailsDto dto){
        Student student = studentRepository.findById(studentId)
                .orElseThrow(()-> new ResourceNotFoundException("Student not found with id :"));

        if(dto.getAccountNumber()== null || dto.getAccountNumber().isEmpty()){
            throw new IllegalArgumentException("Account Number is required");
        }

        if(dto.getIfscCode()==null || dto.getIfscCode().isEmpty()){
            throw new IllegalArgumentException("IFSC code is required");
        }
        List<StudentBankDetails> existingDetails = bankDetailsRepository.findByStudent_UserId(studentId);

        //checks for duplicate account number
        boolean exists = bankDetailsRepository.existsByAccountNumber(dto.getAccountNumber());
        if(exists && (existingDetails.isEmpty() || !existingDetails.get(0).getAccountNumber().equals(dto.getAccountNumber()))){
            throw  new IllegalArgumentException("Account Number already exists !!");
        }

        StudentBankDetails studentBankDetails;
        if(!existingDetails.isEmpty()){
            studentBankDetails = existingDetails.get(0);
        }else {
            studentBankDetails = new StudentBankDetails();
            studentBankDetails.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : java.time.LocalDateTime.now());
        }
        studentBankDetails.setStudent(student);
        studentBankDetails.setAccountHolderName(dto.getAccountHolderName());
        studentBankDetails.setBankName(dto.getBankName());
        studentBankDetails.setAccountNumber(dto.getAccountNumber());
        studentBankDetails.setIfscCode(dto.getIfscCode());
        studentBankDetails.setAccountType(dto.getAccountType());
        studentBankDetails.setBranchName(dto.getBankName());

        StudentBankDetails savedBankDetails = bankDetailsRepository.save(studentBankDetails);
        return userMapper.tostudentBankDetailsDto(savedBankDetails);
    }

    public StudentBankDetailsDto getBankDetailsByStudentId(UUID studentId){
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: "));
        StudentBankDetails studentBankDetails = bankDetailsRepository.findByStudent_UserId(studentId)
                .stream()
                .findFirst()
                .orElseThrow(()->new ResourceNotFoundException("Bank details not found for student with id:"+studentId));

        return userMapper.tostudentBankDetailsDto(studentBankDetails);
    }

    @Transactional
    public void deleteBankDetailsByID(UUID studentId){
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id :"));
        List<StudentBankDetails> bankDetailsList = bankDetailsRepository.findByStudent_UserId(studentId);

        if (bankDetailsList.isEmpty()){
            throw  new ResourceNotFoundException("No bank details found for student with id :");
        }
        bankDetailsRepository.deleteAll(bankDetailsList);
    }

    /*
    public StudentBookingResponseDto createBooking(StudentBookingRequestDto studentBookingRequestDto){
        Student student = studentRepository.findById(studentBookingRequestDto.getStudent())
                .orElseThrow(() ->new ResourceNotFoundException("Student not found with id:"+studentBookingRequestDto.getStudent()));

        Instructor instructor = instructorRepository.findById(studentBookingRequestDto.getInstructor())
                .orElseThrow(()->new ResourceNotFoundException("Instructor not found with id:"+studentBookingRequestDto.getInstructor()));


    }*/

    @Transactional
    public StudentPaymentDto createPayment(UUID studentId, StudentPaymentDto dto){
        Student student = studentRepository.findById(dto.getStudent())
                .orElseThrow(() ->new ResourceNotFoundException("Student not found with id:"+dto.getStudent()));
        StudentBankDetails bankDetails = null;
        if(dto.getStudentBankDetails() != null){
            bankDetails = bankDetailsRepository.findById(dto.getStudentBankDetails())
                    .orElseThrow(()->new ResourceNotFoundException("Bank details not found with id:"+dto.getStudentBankDetails()));
        }

        // Fetch course & instructor
        Course course = null;
        Instructor instructor = null;
        if(dto.getCourseId() != null){
            course = courseRepository.findById(dto.getCourseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course not found with id:"+dto.getCourseId()));
            instructor = course.getInstructor();
        } else {
            throw new IllegalArgumentException("courseId is required for payment");
        }

        // Ensure student enrolled in course before payment
        boolean enrolled = enrollmentRepository.existsByStudent_UserIdAndCourse_CourseId(student.getUserId(), course.getCourseId());
        if (!enrolled) {
            throw new IllegalStateException("Student must be enrolled in course before payment");
        }

        StudentPayment payment = new StudentPayment();
        payment.setStudent(student);
        payment.setStudentBankDetails(bankDetails);
        payment.setCourse(course);
        payment.setAmount(dto.getAmount());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setPaymentStatus(dto.getPaymentStatus() != null ? dto.getPaymentStatus() : "COMPLETED");
        payment.setPaidAt(dto.getPaidAt() !=null ? dto.getPaidAt() : LocalDateTime.now());
        payment.setNotes(dto.getNotes());

        StudentPayment savedPayment = paymentRepository.save(payment);

        // Auto create instructor earning
        if(instructor != null){
            InstructorEarning earning = new InstructorEarning();
            earning.setInstructor(instructor);
            earning.setAmount(savedPayment.getAmount());
            earning.setStudent(student);
            earning.setPaymentMethod(savedPayment.getPaymentMethod());
            // choose first bank detail of instructor if exists
            instructorBankDetailRepository.findByInstructorUserId(instructor.getUserId())
                    .stream().findFirst().ifPresent(earning::setBankDetail);
            earning.setNotes("Auto-generated from student payment #"+ savedPayment.getPaymentId());
            earning.setReceivedAt(LocalDateTime.now());
            instructorEarningRepository.save(earning);
        }

        return userMapper.tostudentPaymentDto(savedPayment);
    }


    public StudentPaymentDto getPaymentById(Integer paymentId){
        StudentPayment payment = paymentRepository.findById(paymentId)
                .orElseThrow(()->new ResourceNotFoundException("Payment not found with id: "));
        return userMapper.tostudentPaymentDto(payment);
    }

    public java.util.List<StudentPaymentDto> getPaymentsByStudent(UUID studentId) {
        return paymentRepository.findAll().stream()
                .filter(p -> p.getStudent() != null && studentId.equals(p.getStudent().getUserId()))
                .map(userMapper::tostudentPaymentDto)
                .toList();
    }
}
