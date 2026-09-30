package com.smart_exam_seating_arrangement.repository;

import com.smart_exam_seating_arrangement.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ExamRepository
        extends JpaRepository<Exam, Long> {

    List<Exam> findByExaminationDate(
            LocalDate examinationDate
    );

    List<Exam>
    findByYearAndBranch1AndSection1OrYearAndBranch2AndSection2(
            Integer year1,
            String branch1,
            String section1,
            Integer year2,
            String branch2,
            String section2
    );
}