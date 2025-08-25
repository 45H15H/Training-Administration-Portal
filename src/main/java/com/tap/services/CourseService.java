package com.tap.services;

import com.tap.dto.CourseCreationDto;
import com.tap.dto.CourseDto;
import com.tap.entities.Course;
import com.tap.entities.Instructor;
import com.tap.entities.InstructorSkill;
import com.tap.entities.ProficiencyLevel;
import com.tap.exceptions.ResourceNotFoundException;
import com.tap.mappers.UserMapper;
import com.tap.repositories.CourseRepository;
import com.tap.repositories.InstructorRepository;
import com.tap.repositories.InstructorSkillRepository;
import com.tap.repositories.ProficiencyLevelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserMapper userMapper;
    private final InstructorRepository instructorRepository;
    private final InstructorSkillRepository instructorSkillRepository;
    private final ProficiencyLevelRepository proficiencyLevelRepository;

    public CourseService(CourseRepository courseRepository, UserMapper userMapper,
                         InstructorRepository instructorRepository, InstructorSkillRepository instructorSkillRepository,
                         ProficiencyLevelRepository proficiencyLevelRepository) {
        this.courseRepository = courseRepository;
        this.userMapper = userMapper;
        this.instructorRepository = instructorRepository;
        this.instructorSkillRepository = instructorSkillRepository;
        this.proficiencyLevelRepository = proficiencyLevelRepository;
    }

    @Transactional
    public CourseDto createCourse(CourseCreationDto courseDto) {
        Instructor instructor = instructorRepository.findById(courseDto.instructorId())
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));
        InstructorSkill skill = instructorSkillRepository.findById(courseDto.skillId())
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));
        ProficiencyLevel level = proficiencyLevelRepository.findById(courseDto.levelId())
                .orElseThrow(() -> new ResourceNotFoundException("Level not found"));

        Course course = new Course();
        course.setInstructor(instructor);
        course.setTitle(courseDto.title());
        course.setDescription(courseDto.description());
        course.setSkill(skill);
        course.setPrice(courseDto.price());
        course.setDuration(courseDto.duration());
        course.setLevel(level);
        course.setIsPublished(courseDto.isPublished());

        Course savedCourse = courseRepository.save(course);
        return userMapper.toCourseDto(savedCourse);
    }

    public CourseDto getCourseById(UUID id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        return userMapper.toCourseDto(course);
    }

    public List<CourseDto> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(userMapper::toCourseDto)
                .collect(Collectors.toList());
    }

        public List<CourseDto> getCoursesByInstructor(UUID instructorId) {
                return courseRepository.findAll().stream()
                                .filter(c -> c.getInstructor() != null && instructorId.equals(c.getInstructor().getUserId()))
                                .map(userMapper::toCourseDto)
                                .collect(Collectors.toList());
        }

        public List<CourseDto> getPublishedCourses() {
                return courseRepository.findAll().stream()
                                .filter(c -> Boolean.TRUE.equals(c.getIsPublished()))
                                .map(userMapper::toCourseDto)
                                .collect(Collectors.toList());
        }

    @Transactional
    public CourseDto updateCourse(UUID id, CourseCreationDto courseDto) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

        Instructor instructor = instructorRepository.findById(courseDto.instructorId())
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));
        InstructorSkill skill = instructorSkillRepository.findById(courseDto.skillId())
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));
        ProficiencyLevel level = proficiencyLevelRepository.findById(courseDto.levelId())
                .orElseThrow(() -> new ResourceNotFoundException("Level not found"));

        course.setInstructor(instructor);
        course.setTitle(courseDto.title());
        course.setDescription(courseDto.description());
        course.setSkill(skill);
        course.setPrice(courseDto.price());
        course.setDuration(courseDto.duration());
        course.setLevel(level);
        course.setIsPublished(courseDto.isPublished());

        Course updatedCourse = courseRepository.save(course);
        return userMapper.toCourseDto(updatedCourse);
    }

    @Transactional
    public void deleteCourse(UUID id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Course not found with id: " + id);
        }
        courseRepository.deleteById(id);
    }
}
