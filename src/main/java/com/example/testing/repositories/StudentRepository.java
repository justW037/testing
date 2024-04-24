package com.example.testing.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.testing.models.Student;

public interface StudentRepository extends JpaRepository<Student, Long>{
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);
    Page<Student> findByOrderByIdAsc(Pageable pageable);
}
