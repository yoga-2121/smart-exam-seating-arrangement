package com.smart_exam_seating_arrangement.Controller;

import com.smart_exam_seating_arrangement.EmailService;
import com.smart_exam_seating_arrangement.entity.Exam;
import com.smart_exam_seating_arrangement.entity.ExamReminderRecipient;
import com.smart_exam_seating_arrangement.entity.Room;
import com.smart_exam_seating_arrangement.entity.SeatingAssignment;
import com.smart_exam_seating_arrangement.entity.Student;
import com.smart_exam_seating_arrangement.repository.ExamReminderRecipientRepository;
import com.smart_exam_seating_arrangement.repository.ExamRepository;
import com.smart_exam_seating_arrangement.repository.RoomRepository;
import com.smart_exam_seating_arrangement.repository.SeatingRepository;
import com.smart_exam_seating_arrangement.repository.StudentRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/seating")
public class SeatingController {

    private final SeatingRepository seatingRepository;
    private final StudentRepository studentRepository;
    private final ExamRepository examRepository;
    private final RoomRepository roomRepository;
    private final EmailService emailService;
    private final ExamReminderRecipientRepository reminderRepository;

    public SeatingController(
            SeatingRepository seatingRepository,
            StudentRepository studentRepository,
            ExamRepository examRepository,
            RoomRepository roomRepository,
            EmailService emailService,
            ExamReminderRecipientRepository reminderRepository) {

        this.seatingRepository = seatingRepository;
        this.studentRepository = studentRepository;
        this.examRepository = examRepository;
        this.roomRepository = roomRepository;
        this.emailService = emailService;
        this.reminderRepository = reminderRepository;
    }

    // =========================================================
    // GENERATE SEATING
    // =========================================================

    @PostMapping("/generate/{examId}")
    public String generateSeating(@PathVariable Long examId) {

        Exam exam =
                examRepository.findById(examId).orElse(null);

        if (exam == null) {
            return "Exam not found";
        }

        List<Student> allStudents =
                studentRepository.findAll();

        List<Student> firstStudents =
                allStudents.stream()
                        .filter(student ->
                                student.getYear().equals(exam.getYear())
                                        && student.getBranch()
                                        .equalsIgnoreCase(exam.getBranch1())
                                        && student.getSection()
                                        .equalsIgnoreCase(exam.getSection1()))
                        .toList();

        List<Student> secondStudents =
                allStudents.stream()
                        .filter(student ->
                                student.getYear().equals(exam.getYear())
                                        && student.getBranch()
                                        .equalsIgnoreCase(exam.getBranch2())
                                        && student.getSection()
                                        .equalsIgnoreCase(exam.getSection2()))
                        .toList();

        List<Student> students =
                new ArrayList<>();

        students.addAll(firstStudents);
        students.addAll(secondStudents);

        if (students.isEmpty()) {
            return "No students found for this exam";
        }

        List<Room> rooms =
                roomRepository.findAll();

        if (rooms.isEmpty()) {
            return "No rooms available";
        }

        int totalCapacity = 0;

        for (Room room : rooms) {

            totalCapacity += room.getCapacity();
        }

        if (totalCapacity < students.size()) {

            return "Not enough room capacity. Students: "
                    + students.size()
                    + ", Capacity: "
                    + totalCapacity;
        }

        // Delete old seating for this exam

        seatingRepository.deleteByExamId(examId);

        int studentIndex = 0;

        for (Room room : rooms) {

            int rows =
                    room.getRows();

            int columns =
                    room.getColumns();

            for (int row = 1; row <= rows; row++) {

                for (int column = 1;
                     column <= columns;
                     column++) {

                    if (studentIndex >= students.size()) {
                        break;
                    }

                    Student student =
                            students.get(studentIndex);

                    SeatingAssignment assignment =
                            new SeatingAssignment();

                    assignment.setExamId(examId);

                    assignment.setRollNumber(
                            student.getRollNumber());

                    assignment.setRoomNumber(
                            room.getRoomNumber());

                    assignment.setSeatNumber(
                            "R" + row + "-C" + column);

                    assignment.setSeatRow(row);

                    assignment.setSeatColumn(column);

                    assignment.setBranch(
                            student.getBranch());

                    assignment.setSection(
                            student.getSection());

                    if (student.getBranch()
                            .equalsIgnoreCase(
                                    exam.getBranch1())
                            &&
                            student.getSection()
                                    .equalsIgnoreCase(
                                            exam.getSection1())) {

                        assignment.setSubject(
                                exam.getSubject1());

                    } else {

                        assignment.setSubject(
                                exam.getSubject2());
                    }

                    seatingRepository.save(
                            assignment);

                    studentIndex++;
                }

                if (studentIndex >= students.size()) {
                    break;
                }
            }

            if (studentIndex >= students.size()) {
                break;
            }
        }

        return "Seating generated successfully for "
                + studentIndex
                + " students.";
    }


    // =========================================================
    // SEND SELECTED STUDENT EMAILS
    // =========================================================

    @PostMapping("/send-selected-emails/{examId}")
    public String sendSelectedEmails(
            @PathVariable Long examId,
            @RequestBody List<String> rollNumbers) {

        Exam exam =
                examRepository
                        .findById(examId)
                        .orElse(null);

        if (exam == null) {
            return "Exam not found";
        }

        if (rollNumbers == null
                || rollNumbers.isEmpty()) {

            return "Please select at least one student";
        }

        List<Student> students =
                new ArrayList<>();

        List<SeatingAssignment> assignments =
                new ArrayList<>();


        // =====================================================
        // FIND VALID STUDENTS AND THEIR SEATING
        // =====================================================

        for (String rollNumber : rollNumbers) {

            Student student =
                    studentRepository
                            .findByRollNumber(rollNumber)
                            .orElse(null);

            if (student == null) {
                continue;
            }

            SeatingAssignment assignment =
                    seatingRepository
                            .findByExamIdAndRollNumber(
                                    examId,
                                    rollNumber)
                            .orElse(null);

            if (assignment == null) {
                continue;
            }

            students.add(student);

            assignments.add(assignment);
        }


        if (students.isEmpty()) {

            return "No valid students found for email sending";
        }


        // =====================================================
        // SAVE SELECTED STUDENTS FOR AUTOMATIC REMINDER
        // =====================================================

        for (String rollNumber : rollNumbers) {

            ExamReminderRecipient existing =
                    reminderRepository
                            .findByExamIdAndRollNumber(
                                    examId,
                                    rollNumber)
                            .orElse(null);


            if (existing == null) {

                ExamReminderRecipient recipient =
                        new ExamReminderRecipient();

                recipient.setExamId(examId);

                recipient.setRollNumber(
                        rollNumber);

                recipient.setReminderSent(
                        false);

                reminderRepository.save(
                        recipient);

            } else {

                // If admin selects the student again,
                // allow the reminder to be sent again.

                existing.setReminderSent(
                        false);

                reminderRepository.save(
                        existing);
            }
        }


        // =====================================================
        // SEND IMMEDIATE SEATING EMAIL
        // =====================================================

        int emailsSent =
                emailService
                        .sendSelectedSeatingEmails(
                                students,
                                assignments);


        return "Selected students: "
                + students.size()
                + ", Emails sent: "
                + emailsSent;
    }


    // =========================================================
    // GET ALL SEATING FOR AN EXAM
    // =========================================================

    @GetMapping("/exam/{examId}")
    public List<SeatingAssignment> getExamSeating(
            @PathVariable Long examId) {

        return seatingRepository
                .findByExamId(examId);
    }


    // =========================================================
    // GET LOGGED-IN STUDENT SEATING
    // =========================================================

    @GetMapping("/student")
    public List<SeatingAssignment> getStudentSeating(
            HttpSession session) {

        Object loggedInRollNumber =
                session.getAttribute(
                        "studentRollNumber");

        if (loggedInRollNumber == null) {

            return List.of();
        }

        String rollNumber =
                loggedInRollNumber.toString();

        return seatingRepository
                .findAll()
                .stream()
                .filter(seat ->
                        seat.getRollNumber()
                                .equalsIgnoreCase(
                                        rollNumber))
                .toList();
    }
}