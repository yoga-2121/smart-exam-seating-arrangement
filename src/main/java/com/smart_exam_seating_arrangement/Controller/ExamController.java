package com.smart_exam_seating_arrangement.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart_exam_seating_arrangement.repository.ExamRepository;
import com.smart_exam_seating_arrangement.entity.Exam;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    private final ExamRepository examRepository;

    public ExamController(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    @PostMapping("/save")
    public String saveExam(@RequestBody Exam exam) {
        examRepository.save(exam);
        return "Exam details saved successfully";
    }

    @GetMapping
    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }
}
