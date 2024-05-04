package com.example.testing.controllers;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.testing.DTOs.StudentRegisterDTO;
import com.example.testing.DTOs.StudentUpdateDTO;
import com.example.testing.models.Student;
import com.example.testing.services.student.IStudentService;

public class StudentControllerTest {
    @Mock
    private IStudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private BindingResult bindingResult;
    

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHome() {
        // Arrange
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        List<Student> students = new ArrayList<>();
        students.add(new Student(1L, "John Doe", "2000-02-01", "john@example.com", "123456789", "123 Main St", 1));
        students.add(new Student(2L, "Jane Doe", "2000-02-01", "jane@example.com", "987654321", "456 Elm St", 0));

        Page<Student> studentsPage = new PageImpl<>(students);
        when(studentService.getAllStudents(pageable)).thenReturn(studentsPage);

        // Act
        String viewName = studentController.home(model, page, size);

        // Assert
        assertEquals("home", viewName);
        verify(model).addAttribute("students", studentsPage.getContent());
        verify(model).addAttribute("page", studentsPage);
    }

    @Test
    public void testCreateStudent_Success() throws Exception {
        // Arrange
        StudentRegisterDTO studentRegisterDTO = new StudentRegisterDTO();
        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        String result = studentController.createStudent(studentRegisterDTO, bindingResult, redirectAttributes);

        // Assert
        assertEquals("redirect:/students/home", result);
        verify(studentService).createStudent(studentRegisterDTO);
    }

    @Test
    public void testCreateStudent_WithErrors_RedirectWithErrors() {
        // Arrange
        StudentRegisterDTO studentRegisterDTO = new StudentRegisterDTO();
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(new FieldError("studentRegisterDTO", "email", "Email không đúng định dạng")));

        // Act
        String result = studentController.createStudent(studentRegisterDTO, bindingResult, redirectAttributes);

        // Assert
        assertEquals("redirect:/students/home", result);
        verify(redirectAttributes).addFlashAttribute(eq("errorMessages"), any());
    }

    @Test
    public void testCreateStudent_ExceptionThrown_RedirectWithError() throws Exception {
        // Arrange
        StudentRegisterDTO studentRegisterDTO = new StudentRegisterDTO();
        when(bindingResult.hasErrors()).thenReturn(false);
        when(studentService.createStudent(studentRegisterDTO)).thenThrow(new Exception("Error message"));
        // Act
        String result = studentController.createStudent(studentRegisterDTO, bindingResult, redirectAttributes);

        // Assert
        assertEquals("redirect:/students/home", result);
        verify(redirectAttributes).addFlashAttribute(eq("errorMessages"), any());
    }

    @Test
    public void testDeleteStudent_Success() throws Exception {
        // Arrange
        Long id = 1L;
        // Act
        String result = studentController.delete(id, redirectAttributes);
        // Assert
        assertEquals("redirect:/students/home", result);
        verify(studentService).deleteStudent(id);
    }

    @Test
    public void testDeleteStudent_Exception_RedirectWithError() throws Exception {
        // Arrange
        Long id = 1L;
        doThrow(new Exception("Error message")).when(studentService).deleteStudent(id);

        // Act
        String result = studentController.delete(id, redirectAttributes);

        // Assert
        assertEquals("redirect:/students/home", result);
        verify(redirectAttributes).addFlashAttribute(eq("errorMessages"), any());
    }

    @Test
    public void testUpdateStudent_Success() throws Exception {
        // Arrange
        StudentUpdateDTO studentUpdateDTO = new StudentUpdateDTO();
        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        String result = studentController.updateStudent(studentUpdateDTO, bindingResult, redirectAttributes);

        // Assert
        assertEquals("redirect:/students/home", result);
        verify(studentService).updateStudent(studentUpdateDTO);
    }

    @Test
    public void testUpdateStudent_WithErrors_RedirectWithErrors() {
        // Arrange
        StudentUpdateDTO studentUpdateDTO = new StudentUpdateDTO();
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(new FieldError("studentUpdateDTO", "email", "Email không đúng định dạng")));
       
        // Act
        String result = studentController.updateStudent(studentUpdateDTO, bindingResult, redirectAttributes);

        // Assert
        assertEquals("redirect:/students/home", result);
        verify(redirectAttributes).addFlashAttribute(eq("errorMessages"), any());
    }

    @Test
    public void testUpdateStudent_ServiceThrowsException_RedirectWithErrorMessage() throws Exception {
        // Arrange
        StudentUpdateDTO studentUpdateDTO = new StudentUpdateDTO();
        when(bindingResult.hasErrors()).thenReturn(false);
        when(studentService.updateStudent(studentUpdateDTO)).thenThrow(new Exception("Error message"));
        // Act
        String result = studentController.updateStudent(studentUpdateDTO, bindingResult, redirectAttributes);

        // Assert
        assertEquals("redirect:/students/home", result);
        verify(redirectAttributes).addFlashAttribute(eq("errorMessages"), any());
    }
}
