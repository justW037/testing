package com.example.testing.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "students")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 15)
    private String name;

    @Column(name = "birthday",nullable = false)
    private String birthday;

    @Column(name = "email", length = 30, nullable = false)
    private String email;

    @Column(name = "phone", length = 15, nullable = false)
    private String phone;

    @Column(name = "address", length = 100, nullable = false)
    private String address;

    @Column(name = "sex", length = 1, nullable = false)
    private int sex;
}
