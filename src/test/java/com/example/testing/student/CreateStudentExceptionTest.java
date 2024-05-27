package com.example.testing.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.testing.DTOs.StudentRegisterDTO;
import com.example.testing.DTOs.UserRegisterDTO;
import com.example.testing.controllers.StudentController;
import com.example.testing.exceptions.ExistedResourceException;
import com.example.testing.repositories.StudentRepository;
import com.example.testing.services.student.StudentService;

import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
public class CreateStudentExceptionTest {
    @Mock
    private StudentRepository studentRepository;

    @Mock
    private BindingResult bindingResult;

    @Mock
    RedirectAttributes redirectAttributes;

    @Mock
    HttpSession session;

    @InjectMocks
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @Test
    public void testCreateStudentEmailExists() {
        StudentRegisterDTO studentDTO = new StudentRegisterDTO("John Doe", "2024-04-02", "existig@example.com","12345678", "123 Main St", 1);

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        when(studentRepository.existsByEmail(studentDTO.getEmail())).thenReturn(true);

        assertThrows(ExistedResourceException.class, () -> studentService.createStudent(studentDTO));

        assertEquals("Email đã tồn tại", assertThrows(ExistedResourceException.class, () -> studentService.createStudent(studentDTO)).getMessage());

        assertEquals("redirect:/students/home", studentController.createStudent(studentDTO, bindingResult, redirectAttributes));
         verify(redirectAttributes, times(1)).addFlashAttribute(eq("errorMessages"), any());
    }
}
