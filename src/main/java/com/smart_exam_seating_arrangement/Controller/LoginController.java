package com.smart_exam_seating_arrangement.Controller;

import com.smart_exam_seating_arrangement.entity.Student;
import com.smart_exam_seating_arrangement.repository.AdminRepository;
import com.smart_exam_seating_arrangement.repository.StudentRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LoginController {

    private final AdminRepository adminRepository;
    private final StudentRepository studentRepository;

    public LoginController(
            AdminRepository adminRepository,
            StudentRepository studentRepository) {

        this.adminRepository = adminRepository;
        this.studentRepository = studentRepository;
    }

    // =========================
    // ADMIN LOGIN
    // =========================
    @PostMapping("/admin/login")
    public String adminLogin(
            @RequestParam String adminId,
            @RequestParam String password,
            HttpSession session) {

        return adminRepository
                .findByAdminId(adminId)
                .map(admin -> {

                    if (admin.getPassword().equals(password)) {
                        session.setAttribute("adminId", adminId);
                        return "Admin Login Successful";
                    }

                    return "Invalid Admin Password";
                })
                .orElse("Admin ID not found");
    }

    // =========================
    // STUDENT LOGIN
    // =========================
    @PostMapping("/student/login")
    public String studentLogin(
            @RequestParam String rollNumber,
            @RequestParam String password,
            HttpSession session) {

        return studentRepository
                .findByRollNumber(rollNumber)
                .map(student -> {

                    if (student.getPassword().equals(password)) {

                        session.setAttribute(
                                "studentRollNumber",
                                student.getRollNumber()
                        );

                        return "Student Login Successful";
                    }

                    return "Invalid Student Password";
                })
                .orElse("Roll Number not found");
    }

    // =========================
    // CURRENT STUDENT
    // =========================
    @GetMapping("/student/current")
    public String getCurrentStudent(HttpSession session) {

        Object rollNumber =
                session.getAttribute("studentRollNumber");

        if (rollNumber == null) {
            return "NOT_LOGGED_IN";
        }

        return rollNumber.toString();
    }

    // =========================
    // STUDENT PROFILE
    // =========================
    @GetMapping("/student/profile")
    public Student getStudentProfile(HttpSession session) {

        Object loggedInRollNumber =
                session.getAttribute("studentRollNumber");

        if (loggedInRollNumber == null) {
            return null;
        }

        String rollNumber =
                loggedInRollNumber.toString();

        return studentRepository
                .findByRollNumber(rollNumber)
                .orElse(null);
    }

    // =========================
    // STUDENT LOGOUT
    // =========================
    @PostMapping("/student/logout")
    public String studentLogout(HttpSession session) {

        session.removeAttribute("studentRollNumber");

        return "Student Logged Out";
    }
}