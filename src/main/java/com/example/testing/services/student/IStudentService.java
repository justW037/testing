package com.example.testing.services.student;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.testing.DTOs.StudentRegisterDTO;
import com.example.testing.DTOs.StudentUpdateDTO;
import com.example.testing.models.Student;

public interface IStudentService {
    Student createStudent(StudentRegisterDTO studentDto) throws Exception;
    Page<Student> getAllStudents(Pageable pageable);
    void deleteStudent(Long studentId) throws Exception;
    Student updateStudent(StudentUpdateDTO studentDto) throws Exception;
}
