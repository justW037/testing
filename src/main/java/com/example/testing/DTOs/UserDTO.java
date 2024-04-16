package com.example.testing.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
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
public class UserDTO {

    private String username;

    @NotBlank(message = "Password không được để trống")
    @Size(min = 8, max = 12, message = "Password phải có độ dài từ 8 đến 12 ký tự")

    private String password;
    private String retypePassword;

    @Email
    @NotBlank(message = "Email không được để trống")
    private String email;

}