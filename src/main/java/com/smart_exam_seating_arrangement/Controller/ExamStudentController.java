package com.smart_exam_seating_arrangement.Controller;

import com.smart_exam_seating_arrangement.entity.Exam;
import com.smart_exam_seating_arrangement.entity.Student;
import com.smart_exam_seating_arrangement.repository.ExamRepository;
import com.smart_exam_seating_arrangement.repository.StudentRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class ExamStudentController {

    private final StudentRepository studentRepository;
    private final ExamRepository examRepository;

    public ExamStudentController(
            StudentRepository studentRepository,
            ExamRepository examRepository) {

        this.studentRepository = studentRepository;
        this.examRepository = examRepository;
    }

    @GetMapping("/exams")
    public List<Exam> getStudentExams(
            @RequestParam String rollNumber) {

        Student student = studentRepository
                .findByRollNumber(rollNumber)
                .orElse(null);

        if (student == null) {
            return List.of();
        }

        return examRepository
                .findByYearAndBranch1AndSection1OrYearAndBranch2AndSection2(
                        student.getYear(),
                        student.getBranch(),
                        student.getSection(),
                        student.getYear(),
                        student.getBranch(),
                        student.getSection()
                );
    }
}