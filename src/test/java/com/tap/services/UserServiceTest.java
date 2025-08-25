package com.tap.services;

import com.tap.dto.UserDto;
import com.tap.entities.Instructor;
import com.tap.entities.Student;
import com.tap.entities.User;
import com.tap.mappers.UserMapper;
import com.tap.repositories.InstructorRepository;
import com.tap.repositories.StudentRepository;
import com.tap.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private InstructorRepository instructorRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private UUID userId;
    private UserDto userDto;


        @BeforeEach
        void setUp() {
            userId = UUID.randomUUID();
            user = new User();
            user.setUserId(userId);
            user.setUsername("testuser");
            user.setEmail("test@example.com");
            user.setAuthorization(false);

            userDto = new UserDto();
            userDto.setUserId(userId);
            userDto.setUsername("testuser");
            userDto.setEmail("test@example.com");
        }

        @Test
        void testCreateUser_Success() {
            when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
            when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
            when(userRepository.save(user)).thenReturn(user);
            when(userMapper.toDto(user)).thenReturn(userDto);

            UserDto result = userService.createUser(user);

            assertNotNull(result);
            assertEquals(userDto.getUsername(), result.getUsername());
        }

        @Test
        void testCreateUser_UsernameExists() {
            when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

            assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
        }

        @Test
        void testCreateUser_EmailExists() {
            when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
            when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

            assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
        }

        @Test
        void testGetUserById_Success() {
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(userMapper.toDto(user)).thenReturn(userDto);

            UserDto result = userService.getUserById(userId);

            assertNotNull(result);
            assertEquals(userDto.getUserId(), result.getUserId());
        }

        @Test
        void testGetUserById_NotFound() {
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class, () -> userService.getUserById(userId));
        }

        @Test
        void testGetAllUsers() {
            List<User> users = List.of(user);
            when(userRepository.findAll()).thenReturn(users);
            when(userMapper.toDto(user)).thenReturn(userDto);

            List<UserDto> result = userService.getAllUsers();

            assertEquals(1, result.size());
            assertEquals(userDto.getUserId(), result.get(0).getUserId());
        }

        @Test
        void testGetAllStudents() {
            Student student = new Student();
            student.setUserId(userId);
            List<Student> students = List.of(student);

            when(studentRepository.findAll()).thenReturn(students);
            when(userMapper.toDto(student)).thenReturn(userDto);

            List<UserDto> result = userService.getAllStudents();

            assertEquals(1, result.size());
        }

        @Test
        void testGetAllInstructors() {
            Instructor instructor = new Instructor();
            instructor.setUserId(userId);
            List<Instructor> instructors = List.of(instructor);

            when(instructorRepository.findAll()).thenReturn(instructors);
            when(userMapper.toDto(instructor)).thenReturn(userDto);

            List<UserDto> result = userService.getAllInstructors();

            assertEquals(1, result.size());
        }

        @Test
        void testUpdateAuthorization_Success() {
            Student student = new Student();
            student.setUserId(userId);
            Instructor instructor = new Instructor();
            instructor.setUserId(userId);

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(studentRepository.findById(userId)).thenReturn(Optional.of(student));
            when(instructorRepository.findById(userId)).thenReturn(Optional.of(instructor));
            when(userRepository.save(user)).thenReturn(user);
            when(userMapper.toDto(user)).thenReturn(userDto);

            UserDto result = userService.updateAuthorization(userId, true);

            assertTrue(user.getAuthorization());
            assertTrue(student.getIsVerified());
            assertTrue(instructor.getIsVerified());
            assertEquals(userDto.getUserId(), result.getUserId());
        }

        @Test
        void testUpdateAuthorization_UserNotFound() {
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class, () -> userService.updateAuthorization(userId, true));
        }

        @Test
        void testDeleteUser_Success() {
            when(userRepository.existsById(userId)).thenReturn(true);
            when(studentRepository.findById(userId)).thenReturn(Optional.empty());
            when(instructorRepository.findById(userId)).thenReturn(Optional.empty());

            assertDoesNotThrow(() -> userService.deleteUser(userId));
            verify(userRepository).deleteById(userId);
        }

        @Test
        void testDeleteUser_NotFound() {
            when(userRepository.existsById(userId)).thenReturn(false);

            assertThrows(RuntimeException.class, () -> userService.deleteUser(userId));
        }
    }


