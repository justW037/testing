package com.example.testing.DTOs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class StudentUpdateDTOTest {
    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidStudent() {
        StudentUpdateDTO studentUpdateDTO = new StudentUpdateDTO(1L, "name", LocalDate.of(2000, 1, 1), "email@domain.com","123456789", "123 Main St", 1);
        assertTrue(validator.validate(studentUpdateDTO).isEmpty());      
    }

    @Test
    public void testInvalidName_TooLong() {
        StudentUpdateDTO studentUpdateDTO = new StudentUpdateDTO(1L, "This is too long student name", LocalDate.of(2000, 1, 1), "email@domain.com","123456789", "123 Main St", 1);
        assertFalse(validator.validate(studentUpdateDTO).isEmpty());  
    }

    @Test
    public void testInvalidName_Empty() {
        StudentUpdateDTO studentUpdateDTO = new StudentUpdateDTO(1L, "", LocalDate.of(2000, 1, 1), "email@domain.com","123456789", "123 Main St", 1);
        assertFalse(validator.validate(studentUpdateDTO).isEmpty());  
    }

    @Test
    public void testInvalidEmail_Empty() {
        StudentUpdateDTO studentUpdateDTO = new StudentUpdateDTO(1L, "name", LocalDate.of(2000, 1, 1), "","123456789", "123 Main St", 1);
        assertFalse(validator.validate(studentUpdateDTO).isEmpty());  
    }

    @Test
    public void testInvalidEmail_InvalidFormat() {
        StudentUpdateDTO studentUpdateDTO = new StudentUpdateDTO(1L, "name", LocalDate.of(2000, 1, 1), "email","123456789", "123 Main St", 1);
        assertFalse(validator.validate(studentUpdateDTO).isEmpty());  
    }

    @Test
    public void testInvalidEmail_TooLong() {
        StudentUpdateDTO studentUpdateDTO = new StudentUpdateDTO(1L, "name", LocalDate.of(2000, 1, 1), "thisemailistoolong@abc.com.vn.edu","123456789", "123 Main St", 1);
        assertFalse(validator.validate(studentUpdateDTO).isEmpty()); 
    }

    @Test
    public void testInvalidPhone_TooLong() {
        StudentUpdateDTO studentUpdateDTO = new StudentUpdateDTO(1L, "name", LocalDate.of(2000, 1, 1), "email@abc.com","123456789123456789", "123 Main St", 1);
        assertFalse(validator.validate(studentUpdateDTO).isEmpty()); 
    }

    @Test
    public void testInvalidAddress_TooLong() {
        StudentUpdateDTO studentUpdateDTO = new StudentUpdateDTO(1L, "name", LocalDate.of(2000, 1, 1), "email@abc.com","123456789", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus.", 1);
        assertFalse(validator.validate(studentUpdateDTO).isEmpty()); 
    }

    @Test
    public void testInvalidSex_Negative() {
        StudentUpdateDTO studentUpdateDTO = new StudentUpdateDTO(1L, "name", LocalDate.of(2000, 1, 1), "email@abc.com","123456789123456789", "123 Main St", -2);
        assertFalse(validator.validate(studentUpdateDTO).isEmpty()); 
    }

    @Test
    public void testBuilderAnnotation() {
        StudentUpdateDTO studentUpdateDTO = StudentUpdateDTO.builder()
        .id(1L)
        .name("name")
        .birthday(LocalDate.of(2000, 1, 1))
        .email("email@abc.com")
        .phone("123456789")
        .address("123 Main St")
        .sex(1)
        .build();
        assertNotNull(studentUpdateDTO);
    }

    @Test
    public void testGetterSetter() {
        // Arrange
        StudentUpdateDTO student = new StudentUpdateDTO();
        Long id = 1L;
        String name = "John Doe";
        LocalDate birthday = LocalDate.now();
        String email = "john.doe@example.com";
        String phone = "123-456-7890";
        String address = "123 Main St";
        int sex = 1;

        // Act
        student.setId(id);
        student.setName(name);
        student.setBirthday(birthday);
        student.setEmail(email);
        student.setPhone(phone);
        student.setAddress(address);
        student.setSex(sex);

        Long actualId = student.getId();
        String actualName = student.getName();
        LocalDate actualBirthday = student.getBirthday();
        String actualEmail = student.getEmail();
        String actualPhone = student.getPhone();
        String actualAddress = student.getAddress();
        Integer actualSex = student.getSex();

        // Assert
        assertEquals(id, actualId);
        assertEquals(name, actualName);
        assertEquals(birthday, actualBirthday);
        assertEquals(email, actualEmail);
        assertEquals(phone, actualPhone);
        assertEquals(address, actualAddress);
        assertEquals(sex, actualSex);
    }

    @Test void testEqualsAndHashCode() {
        // Arrange
        StudentUpdateDTO student1 = new StudentUpdateDTO(1L, "name", LocalDate.of(2000, 1, 1), "email@test.com", "123456789", "123 Main St", 1);

        StudentUpdateDTO student2 = new StudentUpdateDTO(1L, "name", LocalDate.of(2000, 1, 1), "email@test.com", "123456789", "123 Main St", 1);

        assertEquals(student1, student2);
        assertEquals(student1.hashCode(), student2.hashCode());
    }
}
