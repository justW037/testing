package com.example.testing.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.testing.DTOs.StudentRegisterDTO;
import com.example.testing.DTOs.StudentUpdateDTO;
import com.example.testing.exceptions.DataNotFoundException;
import com.example.testing.exceptions.ExistedResourceException;
import com.example.testing.models.Student;
import com.example.testing.repositories.StudentRepository;
import com.example.testing.services.student.StudentService;

public class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateStudent_Success() throws Exception {
        // Arrange
        StudentRegisterDTO studentDTO = new StudentRegisterDTO("John Doe", LocalDate.of(2000, 1, 1), "newemail@example.com","123456789", "123 Main St", 1);
        when(studentRepository.existsByEmail(studentDTO.getEmail())).thenReturn(false);

        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> {
            Student student = invocation.getArgument(0);
            student.setId(1L);
            return student;
        });

        // Act
        Student createdStudent = studentService.createStudent(studentDTO);

        // Assert
        assertNotNull(createdStudent);
        assertEquals("newemail@example.com", createdStudent.getEmail());
        assertEquals("John Doe", createdStudent.getName());
        assertEquals("123 Main St", createdStudent.getAddress());
        assertEquals("2000-01-01", createdStudent.getBirthday().toString());
        assertEquals("123456789", createdStudent.getPhone());
        assertEquals(1, createdStudent.getSex());
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    public void testCreateStudent_EmailExists_ThrowsException() {
        // Arrange
        StudentRegisterDTO studentDTO = new StudentRegisterDTO();
        studentDTO.setEmail("existing@example.com");

        when(studentRepository.existsByEmail(studentDTO.getEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(ExistedResourceException.class, () -> studentService.createStudent(studentDTO), "Email đã tồn tại");
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    public void testGetAllStudents_Success() {
        // Arrange
        List<Student> studentsList = new ArrayList<>();
        studentsList.add(new Student(1L, "John Doe", "2000-02-01", "john@example.com", "123456789", "123 Main St", 1));
        studentsList.add(new Student(2L, "Jane Doe", "2000-02-01", "jane@example.com", "987654321", "456 Elm St", 0));
        

        Pageable pageable = PageRequest.of(0, 10);
        Page<Student> studentsPage = new PageImpl<>(studentsList);

        when(studentRepository.findByOrderByIdAsc(pageable)).thenReturn(studentsPage);

        // Act
        Page<Student> result = studentService.getAllStudents(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(1L, result.getContent().get(0).getId());
        assertEquals("John Doe", result.getContent().get(0).getName());
        assertEquals("john@example.com", result.getContent().get(0).getEmail());
        assertEquals("123 Main St", result.getContent().get(0).getAddress());
        assertEquals("2000-02-01", result.getContent().get(0).getBirthday());
        assertEquals("123456789", result.getContent().get(0).getPhone());
        assertEquals(1, result.getContent().get(0).getSex());
        assertEquals(2L, result.getContent().get(1).getId());
        assertEquals("Jane Doe", result.getContent().get(1).getName());
        assertEquals("jane@example.com", result.getContent().get(1).getEmail());
        assertEquals("456 Elm St", result.getContent().get(1).getAddress());
        assertEquals("2000-02-01", result.getContent().get(1).getBirthday());
        assertEquals("987654321", result.getContent().get(1).getPhone());
        assertEquals(0, result.getContent().get(1).getSex());
    }

    @Test
    public void testDeleteStudent_Success() throws Exception {
        Long studentId = 1L;
        when(studentRepository.existsById(studentId)).thenReturn(true);
        // Act
        studentService.deleteStudent(studentId);
        // Assert
        verify(studentRepository, times(1)).deleteById(studentId);
    }

    @Test
    public void testDeleteStudent_StudentNotFound_ThrowsException() {
        Long studentId = 1L;
        when(studentRepository.existsById(studentId)).thenReturn(false);
        // Act & Assert
        assertThrows(DataNotFoundException.class, () -> studentService.deleteStudent(studentId), "Không tìm thấy sinh viên");
        verify(studentRepository, never()).deleteById(studentId);
    }

    @Test
    public void testUpdateStudent_Success() throws Exception {
        // Arrange
        StudentUpdateDTO studentDTO = new StudentUpdateDTO(1L, "John Doe", LocalDate.of(2000, 1, 1), "john@example.com", "123456789", "123 Main St", 1);
        when(studentRepository.existsByEmailAndIdNot(studentDTO.getEmail(), studentDTO.getId())).thenReturn(false);
        when(studentRepository.findById(studentDTO.getId())).thenReturn(java.util.Optional.of(new Student()));
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> {
            Student student = invocation.getArgument(0);
            student.setId(1L);
            return student;
        });

        Student updatedStudent = studentService.updateStudent(studentDTO);

        // Assert
        assertNotNull(updatedStudent);
        assertEquals(studentDTO.getId(), updatedStudent.getId());
        assertEquals(studentDTO.getName(), updatedStudent.getName());
        assertEquals(studentDTO.getEmail(), updatedStudent.getEmail());
        assertEquals(studentDTO.getPhone(), updatedStudent.getPhone());
        assertEquals(studentDTO.getAddress(), updatedStudent.getAddress());
        assertEquals(studentDTO.getBirthday().toString(), updatedStudent.getBirthday());
        assertEquals(studentDTO.getSex(), updatedStudent.getSex());
    }

    @Test
    public void testUpdateStudent_EmailExists_ThrowsException() {
        // Arrange
        StudentUpdateDTO studentDTO = new StudentUpdateDTO();
        studentDTO.setId(1L);
        studentDTO.setEmail("test@gmail.com");

        when(studentRepository.existsByEmailAndIdNot(studentDTO.getEmail(), studentDTO.getId())).thenReturn(true);

        // Act & Assert
        assertThrows(ExistedResourceException.class, () -> studentService.updateStudent(studentDTO), "Email đã tồn tại.");
    }

    @Test
    public void testUpdateStudent_StudentNotFound_ThrowsException() {
        // Arrange
        StudentUpdateDTO studentDTO = new StudentUpdateDTO();
        studentDTO.setId(1L);
        studentDTO.setEmail("newemail@gmail.com");
            
        when(studentRepository.existsByEmailAndIdNot(studentDTO.getEmail(), studentDTO.getId())).thenReturn(false);
        when(studentRepository.findById(studentDTO.getId())).thenReturn(java.util.Optional.empty());
            // Act & Assert
        assertThrows(DataNotFoundException.class, () -> studentService.updateStudent(studentDTO), "Không tìm thấy sinh viên");
    }
    
}
