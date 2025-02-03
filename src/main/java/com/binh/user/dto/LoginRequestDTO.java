package com.binh.user.dto;

import com.binh.user.constants.UserExceptionMessage;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginRequestDTO {
    @NotBlank(message = UserExceptionMessage.EMAIL_NOT_BLANK)
    private String email;

    @NotBlank(message = UserExceptionMessage.PASSWORD_NOT_BLANK)
    private String password;
}
