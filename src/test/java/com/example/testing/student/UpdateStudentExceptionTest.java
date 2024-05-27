package com.example.testing.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.testing.DTOs.StudentRegisterDTO;
import com.example.testing.DTOs.StudentUpdateDTO;
import com.example.testing.controllers.StudentController;
import com.example.testing.exceptions.DataNotFoundException;
import com.example.testing.exceptions.ExistedResourceException;
import com.example.testing.repositories.StudentRepository;
import com.example.testing.services.student.StudentService;

import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
public class UpdateStudentExceptionTest {
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
    public void testUpdateStudentEmailExists() {
        StudentUpdateDTO studentDTO = new StudentUpdateDTO(1L,"John Doe", "2024-04-02", "newemail@example.com","123456789", "123 Main St", 1);

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        when(studentRepository.existsByEmailAndIdNot(studentDTO.getEmail(), studentDTO.getId())).thenReturn(true);

        assertThrows(ExistedResourceException.class, () -> studentService.updateStudent(studentDTO));

        assertEquals("Email đã tồn tại", assertThrows(ExistedResourceException.class, () -> studentService.updateStudent(studentDTO)).getMessage());

        assertEquals("redirect:/students/home", studentController.updateStudent(studentDTO, bindingResult, redirectAttributes));
         verify(redirectAttributes, times(1)).addFlashAttribute(eq("errorMessages"), any());
    }

    @Test
    public void testUpdateStudentNotFound() {
        StudentUpdateDTO studentDTO = new StudentUpdateDTO(1L,"John Doe", "2024-04-02", "newemail@example.com","123456789", "123 Main St", 1);

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        when(studentRepository.existsByEmailAndIdNot(studentDTO.getEmail(), studentDTO.getId())).thenReturn(false);
        when(studentRepository.findById(studentDTO.getId())).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> studentService.updateStudent(studentDTO));

        assertEquals("Không tìm thấy sinh viên", assertThrows(DataNotFoundException.class, () -> studentService.updateStudent(studentDTO)).getMessage());

        assertEquals("redirect:/students/home", studentController.updateStudent(studentDTO, bindingResult, redirectAttributes));
         verify(redirectAttributes, times(1)).addFlashAttribute(eq("errorMessages"), any());
    }
}
