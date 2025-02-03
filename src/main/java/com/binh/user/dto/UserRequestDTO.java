package com.binh.user.dto;

import com.binh.user.constants.UserExceptionMessage;
import com.binh.user.entity.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
    @NotBlank(message = UserExceptionMessage.EMAIL_NOT_BLANK)
    @Email(message = UserExceptionMessage.EMAIL_INVALID)
    private String email;

    @NotBlank(message = UserExceptionMessage.PASSWORD_NOT_BLANK)
    private String password;

    @NotNull(message = UserExceptionMessage.ROLE_NOT_BLANK)
    private Role role;
}
