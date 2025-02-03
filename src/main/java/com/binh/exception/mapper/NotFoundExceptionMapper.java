package com.binh.exception.mapper;

import com.binh.response.ResponseModel;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    private static final Logger logger = LoggerFactory.getLogger(NotFoundException.class);

    @Override
    public Response toResponse(NotFoundException e) {
        logger.error("NotFoundException: {}", e.getMessage());

        return Response.status(Response.Status.NOT_FOUND)
                .entity(ResponseModel.builder().message(e.getMessage()).build())
                .build();
    }
}
