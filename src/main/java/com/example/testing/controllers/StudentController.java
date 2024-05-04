package com.example.testing.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.testing.DTOs.StudentRegisterDTO;
import com.example.testing.DTOs.StudentUpdateDTO;
import com.example.testing.models.Student;
import com.example.testing.services.student.IStudentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
@RequestMapping("students")
@RequiredArgsConstructor
public class StudentController {
    private final IStudentService studentService;

    @GetMapping("/home")
    public String home(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Student> studentsPage = studentService.getAllStudents(pageable);
        model.addAttribute("students", studentsPage.getContent());
        model.addAttribute("page", studentsPage);
        return "home";
    }

    @PostMapping("/add-student")
    public String createStudent(@Valid StudentRegisterDTO studentRegisterDTO, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
                    redirectAttributes.addFlashAttribute("errorMessages", errorMessages);
                    return "redirect:/students/home";
        }
        try {
            studentService.createStudent(studentRegisterDTO);
            return "redirect:/students/home";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessages", e.getMessage());
            return "redirect:/students/home";
        }
    }
    
    @GetMapping("/delete-student")
    public String delete(@RequestParam Long id, RedirectAttributes redirectAttributes){
        try {
            studentService.deleteStudent(id);
            return "redirect:/students/home";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessages", e.getMessage());
            return "redirect:/students/home";
        }
    }

    @PostMapping("/update-student")
    public String updateStudent(@Valid StudentUpdateDTO studentUpdateDTO , BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
                    redirectAttributes.addFlashAttribute("errorMessages", errorMessages);
                    return "redirect:/students/home";
        }
        try {
            studentService.updateStudent(studentUpdateDTO);
            return "redirect:/students/home";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessages", e.getMessage());
            return "redirect:/students/home";
        }
    }
}
