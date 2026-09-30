package com.smart_exam_seating_arrangement;

import com.smart_exam_seating_arrangement.entity.SeatingAssignment;
import com.smart_exam_seating_arrangement.entity.Student;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    // =========================================================
    // SEND SELECTED SEATING EMAILS
    // =========================================================

    public int sendSelectedSeatingEmails(
            List<Student> students,
            List<SeatingAssignment> assignments) {

        List<MimeMessage> messages = new ArrayList<>();

        for (int i = 0; i < students.size(); i++) {

            Student student = students.get(i);
            SeatingAssignment seating = assignments.get(i);

            try {

                MimeMessage message =
                        mailSender.createMimeMessage();

                MimeMessageHelper helper =
                        new MimeMessageHelper(message, false);

                helper.setTo(student.getEmail());

                helper.setSubject(
                        "Smart Exam Seating Arrangement - Seating Details"
                );

                String emailBody =
                        "Dear Student,\n\n"

                        + "Your examination seating details are given below:\n\n"

                        + "Roll Number : "
                        + student.getRollNumber()
                        + "\n"

                        + "Branch      : "
                        + seating.getBranch()
                        + "\n"

                        + "Section     : "
                        + seating.getSection()
                        + "\n"

                        + "Subject     : "
                        + seating.getSubject()
                        + "\n"

                        + "Room Number : "
                        + seating.getRoomNumber()
                        + "\n"

                        + "Seat Number : "
                        + seating.getSeatNumber()
                        + "\n"

                        + "Row         : "
                        + seating.getSeatRow()
                        + "\n"

                        + "Column      : "
                        + seating.getSeatColumn()
                        + "\n\n"

                        + "Please report to the examination hall on time.\n\n"

                        + "Best Regards,\n"
                        + "Smart Exam Seating Arrangement System";

                helper.setText(emailBody);

                messages.add(message);

            } catch (MessagingException e) {

                System.out.println(
                        "Could not prepare email for "
                                + student.getRollNumber()
                );

                System.out.println(
                        "Reason: "
                                + e.getMessage()
                );
            }
        }


        if (messages.isEmpty()) {
            return 0;
        }


        try {

            mailSender.send(
                    messages.toArray(
                            new MimeMessage[0]
                    )
            );

            return messages.size();

        } catch (MailSendException e) {

            System.out.println(
                    "Email batch sending failed."
            );

            System.out.println(
                    "Reason: "
                            + e.getMessage()
            );

            if (e.getFailedMessages() != null) {

                return messages.size()
                        - e.getFailedMessages().size();
            }

            return 0;
        }
    }


    // =========================================================
    // SEND AUTOMATIC EXAM REMINDER
    // =========================================================

    public void sendExamReminder(
            String studentEmail,
            String rollNumber,
            String examinationType,
            String examinationDate,
            String startTime) {

        try {

            MimeMessage message =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(message, false);

            helper.setTo(studentEmail);

            helper.setSubject(
                    "Exam Reminder - Smart Exam Seating Arrangement"
            );

            String emailBody =
                    "Dear Student,\n\n"

                    + "This is a reminder for your upcoming examination.\n\n"

                    + "Examination Type : "
                    + examinationType
                    + "\n"

                    + "Examination Date : "
                    + examinationDate
                    + "\n"

                    + "Exam Start Time  : "
                    + startTime
                    + "\n"

                    + "Roll Number      : "
                    + rollNumber
                    + "\n\n"

                    + "Your examination starts in 30 minutes.\n\n"

                    + "Please report to the examination hall "
                    + "at least 15 minutes before the examination.\n\n"

                    + "Best Regards,\n"
                    + "Smart Exam Seating Arrangement System";

            helper.setText(emailBody);

            mailSender.send(message);

            System.out.println(
                    "Reminder email sent to "
                            + rollNumber
            );

        } catch (MessagingException e) {

            System.out.println(
                    "Could not send reminder email to "
                            + rollNumber
            );

            System.out.println(
                    "Reason: "
                            + e.getMessage()
            );
        }
    }
}