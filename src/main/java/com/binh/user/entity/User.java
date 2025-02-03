package com.binh.user.entity;

import com.binh.user.constants.UserExceptionMessage;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.*;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = UserExceptionMessage.EMAIL_NOT_BLANK)
    @Email(message = UserExceptionMessage.EMAIL_INVALID)
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = UserExceptionMessage.PASSWORD_NOT_BLANK)
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}