package com.binh.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AddUserRequestDTO {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @Pattern(
        regexp = "^\\+?[0-9]{10,15}$",
        message = "Phone number must be between 10 and 15 digits and can include a leading '+'"
    )
    private String phoneNumber;
}

