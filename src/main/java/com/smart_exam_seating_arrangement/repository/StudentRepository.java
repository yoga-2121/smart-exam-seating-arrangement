package com.smart_exam_seating_arrangement.repository;

import com.smart_exam_seating_arrangement.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository
        extends JpaRepository<Student, Long> {

    Optional<Student> findByRollNumber(String rollNumber);

    List<Student> findByBranchAndSectionAndYear(
            String branch,
            String section,
            Integer year
    );
}