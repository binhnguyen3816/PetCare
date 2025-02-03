package com.binh.user.dto;

import com.binh.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtractToken {
    private String email;
    private Role role;
    private String type;
    private Date date;
}
