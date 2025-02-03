package com.binh.exception.mapper;

import com.binh.response.ResponseModel;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {

    private static final Logger logger = LoggerFactory.getLogger(IllegalArgumentExceptionMapper.class);

    @Override
    public Response toResponse(IllegalArgumentException e) {
        logger.error("IllegalArgumentException: {}", e.getMessage());

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(ResponseModel.builder().message(e.getMessage()).build())
                .build();
    }
}
