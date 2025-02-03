package com.binh.user.rest;

import com.binh.response.ResponseModel;
import com.binh.user.dto.LoginRequestDTO;
import com.binh.user.dto.RefreshRequest;
import com.binh.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("users")
@Tag(name = "User", description = "Operations related to users")
@SecurityScheme(
            name = "bearerAuth", // A name for the scheme
            type = SecuritySchemeType.HTTP, // Type is HTTP (Bearer token)
            scheme = "Bearer", // The scheme is Bearer
            bearerFormat = "JWT" // JWT format if it's a JWT token
)
public class UserRest {

    @Inject
    private UserService userService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Get all users",
            description = "Fetches a list of all users in the system."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of users fetched successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "bearerAuth")
    public Response getAllUsers() {
        return Response.ok().entity(ResponseModel.builder()
                .data(userService.getAllUsers())
                .build()).build();
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Authenticate User",
        description = "Authenticate User by email and password",
        requestBody = @RequestBody(
                description = "Login request with email and password",
                required = true,
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Login Example",
                                value = "{\"email\": \"an@binh.com\", \"password\": \"Aavn123!@#\"}"
                        )
                )
        )
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login successfully"),
        @ApiResponse(responseCode = "401", description = "Authentication failed")
    })
    public Response authenticateUser(@Valid LoginRequestDTO loginRequestDTO) throws NotAuthorizedException {
        return Response.status(Response.Status.OK).entity(ResponseModel.builder()
                .data(userService.authenticate(loginRequestDTO))
                .build()).build();
    }


    @POST
    @Path("refresh")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response refreshToken(@Valid RefreshRequest request) {
        return Response.status(Response.Status.OK).entity(ResponseModel.builder()
                .data(userService.refreshToken(request))
                .build()).build();
    }

}
