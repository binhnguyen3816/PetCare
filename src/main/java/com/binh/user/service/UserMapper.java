package com.binh.user.service;

import com.binh.user.dto.UserRequestDTO;
import com.binh.user.dto.UserResponseDTO;
import com.binh.user.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface UserMapper {
    User toEntity(UserRequestDTO userRequestDTO);

    UserResponseDTO toResponseDTO(User user);

    List<UserResponseDTO> toResponseDTOList(List<User> users);
}
