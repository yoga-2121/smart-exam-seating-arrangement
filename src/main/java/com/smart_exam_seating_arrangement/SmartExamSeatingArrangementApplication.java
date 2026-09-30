package com.smart_exam_seating_arrangement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SmartExamSeatingArrangementApplication {

    public static void main(String[] args) {

        SpringApplication.run(
                SmartExamSeatingArrangementApplication.class,
                args
        );
    }
}