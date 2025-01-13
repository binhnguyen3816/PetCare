package com.binh.user.service;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import com.binh.user.dao.UserDAO;
import com.binh.user.entity.User;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import com.binh.user.dto.AddUserRequestDTO;

@Stateless
public class UserService {
    
    private final UserDAO userDAO;

    @Inject
    public UserService() {
        this.userDAO = new UserDAO();
    }

    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    public User getUserById(Long id) {
        return userDAO.findById(id);
    }

    public User addUser(AddUserRequestDTO addUserRequestDTO) {
        User user = new User();
        user.setFullName(addUserRequestDTO.getFullName());
        user.setEmail(addUserRequestDTO.getEmail());
        
        // Hash the password before saving
        String hashedPassword = BCrypt.hashpw(addUserRequestDTO.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);
        user.setPhoneNumber(addUserRequestDTO.getPhoneNumber());
        return userDAO.save(user);
    }

    public User updateUser(Long id, AddUserRequestDTO addUserRequestDTO) {
        User user = userDAO.findById(id);
        if (user != null) {
            user.setFullName(addUserRequestDTO.getFullName());
            user.setEmail(addUserRequestDTO.getEmail());
            user.setPhoneNumber(addUserRequestDTO.getPhoneNumber());
            // Hash the password before updating
            String hashedPassword = BCrypt.hashpw(addUserRequestDTO.getPassword(), BCrypt.gensalt());
            user.setPassword(hashedPassword);
            return userDAO.update(user);
        }
        return null;
    }

    public void deleteUser(Long id) {
        userDAO.delete(id);
    }
}
