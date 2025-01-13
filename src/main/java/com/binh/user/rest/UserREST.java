package com.binh.user.rest;

import com.binh.user.dto.AddUserRequestDTO;
import com.binh.user.service.UserService;

import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.inject.Inject;

import com.binh.response.ResponseModel;

@Path("users")
public class UserREST {
    
    @Inject
    private UserService userService;

    @GET
    @Produces("application/json")
    public ResponseModel<Object> getAllUsers() {
        return ResponseModel.builder()
                .code(200)
                .data(this.userService.getAllUsers())
                .message(null)
                .build();
    }

    @POST
    @Produces("application/json")
    public ResponseModel<Object> addUser(@Valid AddUserRequestDTO addUserRequestDTO) {
        return ResponseModel.builder()
                .code(200)
                .data(this.userService.addUser(addUserRequestDTO))
                .message(null)
                .build();
    }

    @PUT
    @Path("/{id}")
    @Produces("application/json")
    public ResponseModel<Object> updateUser(@PathParam("id") Long id, @Valid AddUserRequestDTO addUserRequestDTO) {
        return ResponseModel.builder()
                .code(200)
                .data(this.userService.updateUser(id, addUserRequestDTO))
                .message(null)
                .build();
    }

    @DELETE
    @Path("/{id}")
    @Produces("application/json")
    public void deleteUser(@PathParam("id") Long id) {
        userService.deleteUser(id);
    }
}
