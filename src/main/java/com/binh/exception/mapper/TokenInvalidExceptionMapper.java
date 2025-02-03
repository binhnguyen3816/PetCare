package com.binh.exception.mapper;

import com.binh.exception.TemplateException;
import com.binh.exception.TokenInvalidException;
import com.binh.response.ResponseModel;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class TokenInvalidExceptionMapper implements ExceptionMapper<TokenInvalidException> {

    private static final Logger logger = LoggerFactory.getLogger(TokenInvalidException.class);

    @Override
    public Response toResponse(TokenInvalidException e) {
        logger.error("TokenInvalidException: {}", e.getMessage());


        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(ResponseModel.builder().message(e.getMessage()).build())
                .build();
    }
}
