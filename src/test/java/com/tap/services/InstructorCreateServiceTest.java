//package com.tap.services;
//
//import com.tap.dto.InstructorCreationDto;
//import com.tap.dto.InstructorDto;
//import com.tap.entities.Instructor;
//import com.tap.exceptions.DuplicateResourceException;
//import com.tap.exceptions.ResourceNotFoundException;
//import com.tap.mappers.UserMapper;
//import com.tap.repositories.InstructorRepository;
//import com.tap.repositories.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.mockito.Mockito.any;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class InstructorCreateServiceTest {
//
//     @Mock
//     private InstructorRepository instructorRepository;
//
//     @Mock
//     private UserRepository userRepository;
//
//     @Mock
//     private UserMapper userMapper;
//
//     @Mock
//     private PasswordEncoder passwordEncoder;
//
//     @InjectMocks
//     private InstructorService instructorService;
//
//
//
//
//
//    void setUpInstructorCreate() {
//         MockitoAnnotations.openMocks(this);
//     }
//
//     @Test
//     void testCreateInstructor_Success() {
//         InstructorCreationDto dto = new InstructorCreationDto();
//         dto.setUsername("barsha123");
//         dto.setEmail("barsha@example.com");
//         dto.setPassword("securePass");
//         dto.setFirstName("Barsha");
//         dto.setLastName("Parida");
//
//        // Instructor instructor = new Instructor();
//         Instructor savedInstructor = new Instructor();
//         InstructorDto instructorDto = new InstructorDto();
//
//         when(userRepository.findByUsername(dto.getUsername())).thenReturn(Optional.empty());
//         when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
//         when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPass");
//         when(instructorRepository.save(Mockito.any(Instructor.class))).thenReturn(savedInstructor);
//         when(userMapper.toInstructorDto(savedInstructor)).thenReturn(instructorDto);
//
//         InstructorDto result = instructorService.createInstructor(dto);
//
//         assertNotNull(result);
//         verify(instructorRepository).save(any(Instructor.class));
//     }
//
//     @Test
//     void testCreateInstructor_ShortPassword() {
//         InstructorCreationDto dto = new InstructorCreationDto();
//         dto.setPassword("123");
//
//         ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
//                 instructorService.createInstructor(dto));
//
//         assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
//     }
//
//     @Test
//     void testCreateInstructor_DuplicateUsername() {
//         InstructorCreationDto dto = new InstructorCreationDto();
//         dto.setUsername("barsha123");
//         dto.setEmail("barsha@example.com");
//         dto.setPassword("securePass");
//
//         when(userRepository.findByUsername(dto.getUsername())).thenReturn(Optional.of(new Instructor()));
//
//         assertThrows(DuplicateResourceException.class, () ->
//                 instructorService.createInstructor(dto));
//     }
//
//     @Test
//     void testCreateInstructor_DuplicateEmail() {
//         InstructorCreationDto dto = new InstructorCreationDto();
//         dto.setUsername("barsha123");
//         dto.setEmail("barsha@example.com");
//         dto.setPassword("securePass");
//
//         when(userRepository.findByUsername(dto.getUsername())).thenReturn(Optional.empty());
//         when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(new Instructor()));
//
//         assertThrows(DuplicateResourceException.class, () ->
//                 instructorService.createInstructor(dto));
//     }
//
//     @Test
//     void testGetInstructorById_Success() {
//         UUID id = UUID.randomUUID();
//         Instructor instructor = new Instructor();
//         InstructorDto dto = new InstructorDto();
//
//         when(instructorRepository.findById(id)).thenReturn(Optional.of(instructor));
//         when(userMapper.toInstructorDto(instructor)).thenReturn(dto);
//
//         InstructorDto result = instructorService.getInstructorById(id);
//
//         assertNotNull(result);
//         verify(instructorRepository).findById(id);
//     }
//
//     @Test
//     void testGetInstructorById_NotFound() {
//         UUID id = UUID.randomUUID();
//
//         when(instructorRepository.findById(id)).thenReturn(Optional.empty());
//
//         assertThrows(ResourceNotFoundException.class, () ->
//                 instructorService.getInstructorById(id));
//     }
//
//     @Test
//     void testGetAllInstructors() {
//         Instructor instructor = new Instructor();
//         InstructorDto dto = new InstructorDto();
//
//         when(instructorRepository.findAll()).thenReturn(List.of(instructor));
//         when(userMapper.toInstructorDto(instructor)).thenReturn(dto);
//
//         List<InstructorDto> result = instructorService.getAllInstructors();
//
//         assertEquals(1, result.size());
//         verify(instructorRepository).findAll();
//     }
//
//     @Test
//     void testUpdateInstructor_Success() {
//         UUID id = UUID.randomUUID();
//         InstructorCreationDto dto = new InstructorCreationDto();
//         dto.setUsername("barsha123");
//         dto.setEmail("barsha@example.com");
//         dto.setPassword("newPass");
//         dto.setFirstName("Barsha");
//         dto.setLastName("Parida");
//
//         Instructor instructor = new Instructor();
//         instructor.setUsername("oldUser");
//         instructor.setEmail("old@example.com");
//
//         Instructor updatedInstructor = new Instructor();
//         InstructorDto instructorDto = new InstructorDto();
//
//         when(instructorRepository.findById(id)).thenReturn(Optional.of(instructor));
//         when(userRepository.findByUsername(dto.getUsername())).thenReturn(Optional.empty());
//         when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
//         when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPass");
//         when(instructorRepository.save(instructor)).thenReturn(updatedInstructor);
//         when(userMapper.toInstructorDto(updatedInstructor)).thenReturn(instructorDto);
//
//         InstructorDto result = instructorService.updateInstructor(id, dto);
//
//         assertNotNull(result);
//         verify(instructorRepository).save(instructor);
//     }
//
//     @Test
//     void testUpdateInstructor_NotFound() {
//         UUID id = UUID.randomUUID();
//         InstructorCreationDto dto = new InstructorCreationDto();
//
//         when(instructorRepository.findById(id)).thenReturn(Optional.empty());
//
//         assertThrows(ResourceNotFoundException.class, () ->
//                 instructorService.updateInstructor(id, dto));
//     }
// }
//
