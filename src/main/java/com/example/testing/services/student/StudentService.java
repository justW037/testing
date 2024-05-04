package com.example.testing.services.student;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.testing.DTOs.StudentRegisterDTO;
import com.example.testing.DTOs.StudentUpdateDTO;
import com.example.testing.exceptions.DataNotFoundException;
import com.example.testing.exceptions.ExistedResourceException;
import com.example.testing.models.Student;
import com.example.testing.repositories.StudentRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StudentService implements IStudentService{
    private final StudentRepository studentRepository;

    @Override
    public Student createStudent(StudentRegisterDTO studentRegisterDTO) throws Exception{
        String email = studentRegisterDTO.getEmail();

        if(studentRepository.existsByEmail(email)) {
            throw new ExistedResourceException("Email đã tồn tại");
        }
        Student student = Student.builder()
        .email(email)
        .name(studentRegisterDTO.getName())
        .address(studentRegisterDTO.getAddress())
        .birthday(studentRegisterDTO.getBirthday().toString())
        .phone(studentRegisterDTO.getPhone())
        .sex(studentRegisterDTO.getSex())
        .build();
        return studentRepository.save(student);
    }

    @Override
    public Page<Student> getAllStudents(Pageable pageable){
        return studentRepository.findByOrderByIdAsc(pageable);
    }

    @Override
    public void deleteStudent(Long studentId) throws Exception{
        if(!studentRepository.existsById(studentId)) {
            throw new DataNotFoundException("Không tìm thấy sinh viên");
        }
        studentRepository.deleteById(studentId);
    }

    @Override
    public Student updateStudent(StudentUpdateDTO studentDto) throws Exception{
        String email = studentDto.getEmail();
        Long studentId = studentDto.getId();
        if (studentRepository.existsByEmailAndIdNot(email, studentId)) {
            throw new ExistedResourceException("Email đã tồn tại");
        }
        Student student = studentRepository.findById(studentDto.getId()).orElseThrow(() -> new DataNotFoundException("Không tìm thấy sinh viên"));

        student.setEmail(email);
        student.setName(studentDto.getName());
        student.setAddress(studentDto.getAddress());
        student.setBirthday(studentDto.getBirthday().toString());
        student.setPhone(studentDto.getPhone());
        student.setSex(studentDto.getSex());
        
        return studentRepository.save(student);
    }
}
