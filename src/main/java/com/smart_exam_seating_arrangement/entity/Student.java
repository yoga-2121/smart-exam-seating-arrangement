package com.smart_exam_seating_arrangement.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "roll_number", unique = true, nullable = false)
    private String rollNumber;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String section;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String branch;

    @Column(nullable = false)
    private Integer year;

    @Column(name = "student_type", nullable = false)
    private String studentType;

    public Student() {
    }

    public Student(String rollNumber, String password, String section,
                   String email, String branch, Integer year,
                   String studentType) {
        this.rollNumber = rollNumber;
        this.password = password;
        this.section = section;
        this.email = email;
        this.branch = branch;
        this.year = year;
        this.studentType = studentType;
    }

    public Long getId() {
        return id;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getStudentType() {
        return studentType;
    }

    public void setStudentType(String studentType) {
        this.studentType = studentType;
    }
}