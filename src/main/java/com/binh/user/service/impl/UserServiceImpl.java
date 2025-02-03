package com.binh.user.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.binh.exception.InvalidTokenTypeException;
import com.binh.user.constants.UserExceptionMessage;
import com.binh.user.dao.UserDAO;
import com.binh.user.dto.*;
import com.binh.user.entity.Role;
import com.binh.user.entity.User;
import com.binh.user.service.UserMapper;
import com.binh.user.service.UserService;
import com.binh.utils.PasswordHasher;
import com.binh.utils.TokenFactory;
import com.binh.utils.TokenType;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;

@Stateless
public class UserServiceImpl implements UserService {

    @Inject
    private UserDAO userDAO;

    @Inject
    private UserMapper userMapper;

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userDAO.findAll();
        return userMapper.toResponseDTOList(users);
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        User user = userMapper.toEntity(userRequestDTO);
        String password = user.getPassword();
        String hashedPassword = PasswordHasher.hashPassword(password);
        user.setPassword(hashedPassword);
        return userMapper.toResponseDTO(userDAO.save(user));
    }

    @Override
    public LoginResponseDTO authenticate(LoginRequestDTO loginRequestDTO) {
        Optional<User> user = userDAO.findByEmail(loginRequestDTO.getEmail());
        if (user.isPresent()) {
            checkPasswordValid(loginRequestDTO.getPassword(), user.get().getPassword());

            String accessToken = generateAccessToken(loginRequestDTO.getEmail(), user.get().getRole());
            String refreshToken = generateRefreshToken(loginRequestDTO.getEmail(), user.get().getRole());

            UserResponseDTO userResponseDTO = createUserResponseDTO(user.get());
            return new LoginResponseDTO(accessToken, refreshToken, userResponseDTO);
        }

        throw new NotFoundException(UserExceptionMessage.AUTHENTICATION_FAILED);
    }

    private void checkPasswordValid(String inputPassword, String storedPassword) {
        if (!PasswordHasher.hashPassword(inputPassword).equals(storedPassword)) {
            throw new NotAuthorizedException(UserExceptionMessage.AUTHENTICATION_FAILED);
        }
    }

    private String generateAccessToken(String email, Role role) {
        return TokenFactory.generateToken(email, role, TokenType.ACCESS);
    }

    private String generateRefreshToken(String email, Role role) {
        return TokenFactory.generateToken(email, role, TokenType.REFRESH);
    }

    private UserResponseDTO createUserResponseDTO(User user) {
        return new UserResponseDTO(user.getEmail(), user.getRole());
    }

    @Override
    public ExtractToken extractToken(String token) {
        if (token != null) {
            Claims extractClaim = TokenFactory.decodeToken(token);
            return createExtractToken(extractClaim);
        }

        throw new IllegalArgumentException(UserExceptionMessage.TOKEN_INVALID);
    }

    private ExtractToken createExtractToken(Claims extractClaim) {
        String email = extractClaim.getSubject();
        String roleName = extractClaim.get("role", String.class);
        Role role = Role.valueOf(roleName);
        String tokenType = extractClaim.get("type", String.class);
        Date date = extractClaim.getExpiration();

        return new ExtractToken(email, role, tokenType , date);
    }

    @Override
    public Token refreshToken(RefreshRequest request) {
        ExtractToken tokenInfo = extractToken(request.getToken());
        Date expireAt = tokenInfo.getDate();
        String type = tokenInfo.getType();
        if(type.equals(TokenType.ACCESS.name())){
            throw new InvalidTokenTypeException(UserExceptionMessage.TOKEN_INVALID);
        }

        if(expireAt.after(new Date())) {
            String accessToken = generateAccessToken(tokenInfo.getEmail(), tokenInfo.getRole());
            String refreshToken = generateRefreshToken(tokenInfo.getEmail(), tokenInfo.getRole());
            return new Token(accessToken, refreshToken);
        }
        throw new ExpiredJwtException(null, null, UserExceptionMessage.TOKEN_EXPIRED);
    }

}
