package com.example.demo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.AppUser;
import com.example.demo.model.Course;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CourseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final UserRepository userRepository;

    //Получение всех курсов
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    // Получение одного курса по ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable Long id) {
        try {
            Course course = courseService.getCourseById(id);
            return ResponseEntity.ok(course);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //Обновление курса (только для ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id, @RequestBody Course updatedCourse) {
        try {
            Course course = courseService.updateCourse(id, updatedCourse);
            return ResponseEntity.ok(course);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Удаление курса (только для ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        try {
            courseService.deleteCourse(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    //Регистрация на курс
    @PostMapping("/{courseId}/register")
    public ResponseEntity<String> registerToCourse(@PathVariable Long courseId, Principal principal) {
        try {
            AppUser user = userRepository.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

            courseService.registerUserToCourse(courseId, user);

            return ResponseEntity.ok("✅ Вы успешно записаны на курс!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Ошибка регистрации: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Внутренняя ошибка: " + e.getMessage());
        }
    }
    //Удаление с курса
    @DeleteMapping("/{courseId}/remove/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> removeUserFromCourse(
            @PathVariable Long courseId,
            @PathVariable Long userId) {
        try {
            AppUser user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
            courseService.removeUserFromCourse(courseId, user);
            return ResponseEntity.ok("🗑️ Пользователь удалён с курса");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Ошибка: " + e.getMessage());
        }
    }


    //Просмотр студентов
    @GetMapping("/{courseId}/students")
    public ResponseEntity<List<AppUser>> findStudentsByCourse(
            @PathVariable Long courseId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email) {
    
        List<AppUser> users;
        if (name == null && email == null) {
            users = userRepository.findUsersByCourseOnly(courseId);
        } else {
            users = userRepository.findUsersByCourseAndFilter(courseId,
                    name == null ? "" : name,
                    email == null ? "" : email);
        }
    
        return ResponseEntity.ok(users);
    }
    



}
