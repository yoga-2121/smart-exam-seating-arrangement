package com.smart_exam_seating_arrangement.repository;

import com.smart_exam_seating_arrangement.entity.ExamReminderRecipient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExamReminderRecipientRepository
        extends JpaRepository<ExamReminderRecipient, Long> {

    List<ExamReminderRecipient> findByExamId(Long examId);

    Optional<ExamReminderRecipient> findByExamIdAndRollNumber(
            Long examId,
            String rollNumber
    );
}