package com.smart_exam_seating_arrangement.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "exam_reminder_recipients")
public class ExamReminderRecipient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "exam_id", nullable = false)
    private Long examId;

    @Column(name = "roll_number", nullable = false)
    private String rollNumber;

    @Column(name = "reminder_sent", nullable = false)
    private boolean reminderSent = false;

    public ExamReminderRecipient() {
    }

    public Long getId() {
        return id;
    }

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public boolean isReminderSent() {
        return reminderSent;
    }

    public void setReminderSent(boolean reminderSent) {
        this.reminderSent = reminderSent;
    }
}
