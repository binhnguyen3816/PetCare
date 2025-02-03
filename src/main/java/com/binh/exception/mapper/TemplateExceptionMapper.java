package com.binh.exception.mapper;

import com.binh.exception.TemplateException;
import com.binh.response.ResponseModel;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class TemplateExceptionMapper implements ExceptionMapper<TemplateException> {

    private static final Logger logger = LoggerFactory.getLogger(TemplateException.class);

    @Override
    public Response toResponse(TemplateException e) {
        logger.error("TemplateException: {}", e.getMessage());

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(ResponseModel.builder().message(e.getMessage()).build())
                .build();
    }
}
