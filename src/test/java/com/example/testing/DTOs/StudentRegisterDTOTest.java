package com.example.testing.DTOs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class StudentRegisterDTOTest {
    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidStudent() {
        StudentRegisterDTO studentRegisterDTO = new StudentRegisterDTO("name", LocalDate.of(2000, 1, 1), "email@domain.com","123456789", "123 Main St", 1);
        assertTrue(validator.validate(studentRegisterDTO).isEmpty());      
    }

    @Test
    public void testInvalidName_Empty() {
        StudentRegisterDTO studentRegisterDTO = new StudentRegisterDTO("", LocalDate.of(2000, 1, 1), "email@domain.com","123456789", "123 Main St", 1);
        assertFalse(validator.validate(studentRegisterDTO).isEmpty());    
    }

    @Test
    public void testInvalidName_TooLong() {
        StudentRegisterDTO studentRegisterDTO = new StudentRegisterDTO("This is too long student name", LocalDate.of(2000, 1, 1), "email@domain.com","123456789", "123 Main St", 1);
        assertFalse(validator.validate(studentRegisterDTO).isEmpty());
    }

    @Test
    public void testInvalidEmail_Empty() {
        StudentRegisterDTO studentRegisterDTO = new StudentRegisterDTO("name", LocalDate.of(2000, 1, 1), "","123456789", "123 Main St", 1);
        assertFalse(validator.validate(studentRegisterDTO).isEmpty());
    }

    @Test
    public void testInvalidEmail_InvalidFormat() {
        StudentRegisterDTO studentRegisterDTO = new StudentRegisterDTO("name", LocalDate.of(2000, 1, 1), "email","123456789", "123 Main St", 1);
        assertFalse(validator.validate(studentRegisterDTO).isEmpty());
    }

    @Test
    public void testInvalidEmail_TooLong() {
        StudentRegisterDTO studentRegisterDTO = new StudentRegisterDTO("name", LocalDate.of(2000, 1, 1), "thisemailistoolong@abc.com.vn.edu","123456789", "123 Main St", 1);
    }

    @Test
    public void testInvalidPhone_TooLong() {
        StudentRegisterDTO studentRegisterDTO = new StudentRegisterDTO("name", LocalDate.of(2000, 1, 1), "email@abc.com","123456789123456789", "123 Main St", 1);
        assertFalse(validator.validate(studentRegisterDTO).isEmpty());
    }

    @Test
    public void testInvalidAddress_TooLong() {
        StudentRegisterDTO studentRegisterDTO = new StudentRegisterDTO("name", LocalDate.of(2000, 1, 1), "email@abc.com","123456789", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus.", 1);
        assertFalse(validator.validate(studentRegisterDTO).isEmpty());
    }

    @Test
    public void testInvalidSex_Negative() {
        StudentRegisterDTO studentRegisterDTO = new StudentRegisterDTO("name", LocalDate.of(2000, 1, 1), "email@abc.com","123456789", "123 Main St", -2);
        assertFalse(validator.validate(studentRegisterDTO).isEmpty());
    }

    @Test
    public void testBuilderAnnotation() {
        StudentRegisterDTO studentRegisterDTO = StudentRegisterDTO.builder().name("name")
        .birthday(LocalDate.of(2000, 1, 1))
        .email("email@abc.com")
        .phone("123456789")
        .address("123 Main St")
        .sex(1)
        .build();
        assertNotNull(studentRegisterDTO);
    }

    
    @Test
    public void testGetterSetter() {
        // Arrange
        StudentRegisterDTO student = new StudentRegisterDTO();
        String name = "John Doe";
        LocalDate birthday = LocalDate.now();
        String email = "john.doe@example.com";
        String phone = "123-456-7890";
        String address = "123 Main St";
        int sex = 1;

        // Act
        student.setName(name);
        student.setBirthday(birthday);
        student.setEmail(email);
        student.setPhone(phone);
        student.setAddress(address);
        student.setSex(sex);

        String actualName = student.getName();
        LocalDate actualBirthday = student.getBirthday();
        String actualEmail = student.getEmail();
        String actualPhone = student.getPhone();
        String actualAddress = student.getAddress();
        Integer actualSex = student.getSex();

        // Assert
        assertEquals(name, actualName);
        assertEquals(birthday, actualBirthday);
        assertEquals(email, actualEmail);
        assertEquals(phone, actualPhone);
        assertEquals(address, actualAddress);
        assertEquals(sex, actualSex);
    }

    @Test void testEqualsAndHashCode() {
        // Arrange
        StudentRegisterDTO student1 = new StudentRegisterDTO("name", LocalDate.of(2000, 1, 1), "email@test.com", "123456789", "123 Main St", 1);

        StudentRegisterDTO student2 = new StudentRegisterDTO("name", LocalDate.of(2000, 1, 1), "email@test.com", "123456789", "123 Main St", 1);

        assertEquals(student1, student2);
        assertEquals(student1.hashCode(), student2.hashCode());
    }
}

