package com.smart_exam_seating_arrangement.repository;

import com.smart_exam_seating_arrangement.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByAdminId(String adminId);
}