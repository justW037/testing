package com.example.testing.student;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.testing.DTOs.StudentRegisterDTO;
import com.example.testing.DTOs.StudentUpdateDTO;
import com.example.testing.controllers.StudentController;
import com.example.testing.repositories.StudentRepository;
import com.example.testing.services.student.IStudentService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@ExtendWith(MockitoExtension.class)
public class UpdateStudentTest {
    @Mock
    private StudentRepository studentRepository;
    
    @Mock
    private IStudentService studentService;

    @Mock
    private BindingResult bindingResult;

    @Mock
    RedirectAttributes redirectAttributes;
    
    @Mock
    HttpSession session;
    
    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    public void studentUpdateValidationTest(StudentUpdateDTO dto, String expectedErrorMessage) throws Exception {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
    
        if (validator.validate(dto).isEmpty()) {
            assertTrue(validator.validate(dto).isEmpty());

            assertEquals("redirect:/students/home", studentController.updateStudent(dto, bindingResult, redirectAttributes));
            // verify(userService).createUser(userRegisterDTO);
        
        } else {
        List<String> expectedErrorMessages = Arrays.asList(expectedErrorMessage.split(";"));    
        Set<ConstraintViolation<StudentUpdateDTO>> violations = validator.validate(dto);
    
        BindingResult result = new MapBindingResult(new HashMap<>(), "userRegisterDTO");
        for (ConstraintViolation<StudentUpdateDTO> violation : violations) {
            result.addError(new FieldError("studentUpdateDTO", violation.getPropertyPath().toString(), violation.getMessage()));
        }

        assertEquals("redirect:/students/home", studentController.updateStudent(dto, result, redirectAttributes));
            
        List<String> actualErrorMessages = result.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        assertEquals(expectedErrorMessages.size(), actualErrorMessages.size());
        Collections.sort(actualErrorMessages);
        Collections.sort(expectedErrorMessages);
        assertArrayEquals(expectedErrorMessages.toArray(), actualErrorMessages.toArray());

        verify(redirectAttributes, times(1)).addFlashAttribute(eq("errorMessages"), anyList());
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/test/student/studentUpdateTest.csv", numLinesToSkip = 1)
    void testStudentRegister(Long id, String name, String birthday, String email, String phone, String address, int sex, String expectedErrorMessage) throws Exception {  
    StudentUpdateDTO dto = StudentUpdateDTO.builder()
        .id(id)
        .name(name)
        .birthday(birthday)
        .email(email)
        .phone(phone)
        .address(address)
        .sex(sex)
        .build();
        studentUpdateValidationTest(dto, expectedErrorMessage);
    }

}
