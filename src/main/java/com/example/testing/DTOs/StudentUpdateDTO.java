package com.example.testing.DTOs;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentUpdateDTO {
    private Long id;

    private String name;
    private LocalDate birthday;

    @Email
    @NotBlank(message = "Email không được để trống")
    private String email;

    private String phone;

    @Size(max = 100, message = "Địa chỉ dưới 100 ký tự")
    private String address;

    @Min(value = 0, message = "Giới tính không hợp lệ")
    private int sex;
}
