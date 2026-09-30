package com.smart_exam_seating_arrangement;

import com.smart_exam_seating_arrangement.entity.SeatingAssignment;
import com.smart_exam_seating_arrangement.entity.Student;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    private final RestTemplate restTemplate;

    @Value("${BREVO_API_KEY}")
    private String brevoApiKey;

    private static final String BREVO_URL =
            "https://api.brevo.com/v3/smtp/email";

    private static final String SENDER_EMAIL =
            "smartexamseating@gmail.com";

    public EmailService() {
        this.restTemplate = new RestTemplate();
    }

    // =========================================================
    // SEND SELECTED SEATING EMAILS
    // =========================================================

    public int sendSelectedSeatingEmails(
            List<Student> students,
            List<SeatingAssignment> assignments) {

        int sentCount = 0;

        int count = Math.min(
                students.size(),
                assignments.size()
        );

        for (int i = 0; i < count; i++) {

            Student student = students.get(i);
            SeatingAssignment seating = assignments.get(i);

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

            boolean sent = sendEmail(
                    student.getEmail(),
                    "Smart Exam Seating Arrangement - Seating Details",
                    emailBody
            );

            if (sent) {
                sentCount++;
            }
        }

        return sentCount;
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

        boolean sent = sendEmail(
                studentEmail,
                "Exam Reminder - Smart Exam Seating Arrangement",
                emailBody
        );

        if (sent) {

            System.out.println(
                    "Reminder email sent to "
                            + rollNumber
            );

        } else {

            System.out.println(
                    "Reminder email failed for "
                            + rollNumber
            );
        }
    }

    // =========================================================
    // BREVO HTTPS API
    // =========================================================

    private boolean sendEmail(
            String recipient,
            String subject,
            String body) {

        try {

            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(
                    MediaType.APPLICATION_JSON
            );

            headers.set(
                    "api-key",
                    brevoApiKey
            );

            // Sender
            Map<String, Object> sender =
                    new HashMap<>();

            sender.put(
                    "name",
                    "Smart Exam Seating Arrangement"
            );

            sender.put(
                    "email",
                    SENDER_EMAIL
            );

            // Recipient
            Map<String, Object> to =
                    new HashMap<>();

            to.put(
                    "email",
                    recipient
            );

            // Request body
            Map<String, Object> request =
                    new HashMap<>();

            request.put(
                    "sender",
                    sender
            );

            request.put(
                    "to",
                    List.of(to)
            );

            request.put(
                    "subject",
                    subject
            );

            request.put(
                    "textContent",
                    body
            );

            HttpEntity<Map<String, Object>> entity =
                    new HttpEntity<>(
                            request,
                            headers
                    );

            restTemplate.postForEntity(
                    BREVO_URL,
                    entity,
                    String.class
            );

            System.out.println(
                    "Email sent successfully to "
                            + recipient
            );

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Email sending failed for "
                            + recipient
            );

            System.out.println(
                    "Reason: "
                            + e.getMessage()
            );

            return false;
        }
    }
}