package com.example.testing.DTOs;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @NotNull(message = "ID không được null")
    @Min(value = 1, message = "ID phải lớn hơn hoặc bằng 1")
    private Long id;

    @Size(max = 15, message = "Tên tối đa 15 ký tự")
    @NotBlank(message = "Tên sv không được để trống")
    private String name;

    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Ngày sinh phải có định dạng yyyy-MM-dd")
    private String birthday;

    @Email(message = "Email không đúng định dạng")
    @NotBlank(message = "Email không được để trống")
    @Size(max = 30, message = "Email tối đa 30 ký tự")
    private String email;

    @Size(max = 15, message = "Số điện thoại tối đa 15 ký tự")
    private String phone;

    @Size(max = 50, message = "Địa chỉ tối đa 50 ký tự")
    private String address;

    @Digits(integer = 1, fraction = 0, message = "Giới tính phải là số có 1 chữ số")
    @Min(value = 0, message = "Giới tính không hợp lệ")
    private int sex;
}
