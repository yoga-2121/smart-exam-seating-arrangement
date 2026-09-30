package com.smart_exam_seating_arrangement;

import com.smart_exam_seating_arrangement.entity.Exam;
import com.smart_exam_seating_arrangement.entity.ExamReminderRecipient;
import com.smart_exam_seating_arrangement.entity.Student;
import com.smart_exam_seating_arrangement.repository.ExamReminderRecipientRepository;
import com.smart_exam_seating_arrangement.repository.ExamRepository;
import com.smart_exam_seating_arrangement.repository.StudentRepository;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
public class ExamReminderScheduler {

    private final ExamRepository examRepository;
    private final ExamReminderRecipientRepository reminderRepository;
    private final StudentRepository studentRepository;
    private final EmailService emailService;

    public ExamReminderScheduler(
            ExamRepository examRepository,
            ExamReminderRecipientRepository reminderRepository,
            StudentRepository studentRepository,
            EmailService emailService) {

        this.examRepository = examRepository;
        this.reminderRepository = reminderRepository;
        this.studentRepository = studentRepository;
        this.emailService = emailService;
    }

    // Check every minute
    @Scheduled(fixedRate = 60000)
    public void sendExamReminders() {

        LocalDate today =
                LocalDate.now();

        LocalTime currentTime =
                LocalTime.now().withSecond(0).withNano(0);

        List<Exam> exams =
                examRepository.findByExaminationDate(today);

        for (Exam exam : exams) {

            LocalTime reminderTime =
                    exam.getStartTime().minusMinutes(30);

            if (!currentTime.equals(reminderTime)) {
                continue;
            }

            List<ExamReminderRecipient> recipients =
                    reminderRepository.findByExamId(
                            exam.getId());

            for (ExamReminderRecipient recipient : recipients) {

                if (recipient.isReminderSent()) {
                    continue;
                }

                Student student =
                        studentRepository
                                .findByRollNumber(
                                        recipient.getRollNumber())
                                .orElse(null);

                if (student == null) {
                    continue;
                }

                emailService.sendExamReminder(
                        student.getEmail(),
                        student.getRollNumber(),
                        exam.getExaminationType(),
                        exam.getExaminationDate().toString(),
                        exam.getStartTime().toString()
                );

                recipient.setReminderSent(true);

                reminderRepository.save(recipient);
            }
        }
    }
}
