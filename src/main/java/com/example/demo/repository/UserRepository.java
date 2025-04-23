package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.AppUser;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);

    @Query("""
                SELECT u FROM AppUser u
                JOIN u.enrolledCourses c
                WHERE c.id = :courseId
            """)
    List<AppUser> findUsersByCourseOnly(@Param("courseId") Long courseId);

    @Query("""
                SELECT u FROM AppUser u
                JOIN u.enrolledCourses c
                WHERE c.id = :courseId
                AND LOWER(u.firstName) LIKE LOWER(CONCAT('%', :name, '%'))
                AND LOWER(u.username) LIKE LOWER(CONCAT('%', :email, '%'))
            """)
    List<AppUser> findUsersByCourseAndFilter(
            @Param("courseId") Long courseId,
            @Param("name") String name,
            @Param("email") String email);

}
