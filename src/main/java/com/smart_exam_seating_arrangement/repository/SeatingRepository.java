package com.smart_exam_seating_arrangement.repository;

import com.smart_exam_seating_arrangement.entity.SeatingAssignment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface SeatingRepository
        extends JpaRepository<SeatingAssignment, Long> {

    List<SeatingAssignment> findByExamId(Long examId);

    Optional<SeatingAssignment> findByExamIdAndRollNumber(
            Long examId,
            String rollNumber
    );

    @Modifying
    @Transactional
    void deleteByExamId(Long examId);
}