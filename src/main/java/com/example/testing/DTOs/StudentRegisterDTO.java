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
public class StudentRegisterDTO {

    @Size(max = 15, message = "Tên tối đa 15 ký tự")
    @NotBlank(message = "Tên sv không được để trống")
    private String name;

    private LocalDate birthday;

    @Email(message = "Email không đúng định dạng")
    @NotBlank(message = "Email không được để trống")
    @Size(max = 30, message = "Email tối đa 30 ký tự")
    private String email;

    @Size(max = 15, message = "Số điện thoại tối đa 15 ký tự")
    private String phone;

    @Size(max = 100, message = "Địa chỉ tối đa 100 ký tự")
    private String address;

    // @Size(max = 1, message = "Giới tính phải là số có 1 chữ số")
    @Min(value = 0, message = "Giới tính không hợp lệ")
    private int sex;
}
