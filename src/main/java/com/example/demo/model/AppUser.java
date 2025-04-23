package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String username; //используется как email

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime registrationDate;

    @ManyToMany(mappedBy = "participants")
    @JsonIgnore //сериализовать обратно
    private List<Course> enrolledCourses = new ArrayList<>();

    @PrePersist
    public void onCreate() {
        this.registrationDate = LocalDateTime.now();
    }

    public boolean isStudent() {
        return !enrolledCourses.isEmpty();
    }
}
