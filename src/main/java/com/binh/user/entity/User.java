package com.binh.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "password")  // Exclude password from toString for security reasons
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Long id;

    @NotBlank
    private String fullName;

    @NotBlank
    @Column(unique = true) // Ensure email uniqueness
    private String email;

    @NotBlank
    private String password;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$") // Simple phone number validation
    private String phoneNumber;
}
