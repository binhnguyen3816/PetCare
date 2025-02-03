package com.binh.user.dto;

import com.binh.user.entity.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayloadToken {
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;
}
