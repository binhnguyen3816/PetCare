package com.binh.user.service;

import java.util.List;

import com.binh.user.dto.*;

import jakarta.ws.rs.NotAuthorizedException;

public interface UserService {
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO createUser(UserRequestDTO userRequestDTO);
    LoginResponseDTO authenticate(LoginRequestDTO loginRequestDTO) throws NotAuthorizedException;
    ExtractToken extractToken(String jwtToken);
    Token refreshToken(RefreshRequest request);
}