package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.email.EmailService;
import com.example.demo.model.AppUser;
import com.example.demo.model.Course;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Курс не найден"));
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public Course updateCourse(Long id, Course updated) {
        Course course = getCourseById(id);
        course.setTitle(updated.getTitle());
        course.setDescription(updated.getDescription());
        course.setStartDate(updated.getStartDate());
        course.setEndDate(updated.getEndDate());
        return courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public void registerUserToCourse(Long courseId, AppUser user) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Курс не найден"));

        course.getParticipants().add(user);
        courseRepository.save(course);


        String subject = "Вы записаны на курс: " + course.getTitle();
        String message = String.format("""
                Здравствуйте, %s!

                Вы успешно записаны на курс "%s".

                📅 Дата начала: %s
                📚 Описание: %s

                Спасибо, что выбрали наш учебный центр!
                """,
                user.getFirstName() != null ? user.getFirstName() : user.getUsername(),
                course.getTitle(),
                course.getStartDate(),
                course.getDescription()
        );

        emailService.sendEmail(user.getUsername(), subject, message);
    }

    public void removeUserFromCourse(Long courseId, AppUser user) {
        Course course = getCourseById(courseId);

        if (course.getParticipants().remove(user)) {
            user.getEnrolledCourses().remove(course);
            courseRepository.save(course);
            userRepository.save(user);
        } else {
            throw new RuntimeException("Студент не найден на этом курсе");
        }
    }
}
