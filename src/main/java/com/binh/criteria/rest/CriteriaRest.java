package com.binh.criteria.rest;

import com.binh.criteria.constants.CriteriaExceptionMessage;
import com.binh.criteria.service.CriteriaService;
import com.binh.response.ResponseModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

@Path("criterias")
@Tag(name = "Criteria", description = "Operations related to criteria")
public class CriteriaRest {

    @Inject
    private CriteriaService criteriaService;

    private static final Logger logger = LoggerFactory.getLogger(CriteriaRest.class);

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Import criteria",
            description = "Imports criteria from a provided file."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Criteria imported successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Response importCriteria(@MultipartForm InputStream fileInputStream,
                                   @HeaderParam("Content-Length") long fileSize) {
        logger.info("Starting importCriteria method");
        logger.debug("File size: {}", fileSize);
        ResponseModel<Object> responseModel = ResponseModel.builder()
                .message(CriteriaExceptionMessage.IMPORT_SUCCESS)
                .data(criteriaService.importCriteria(fileInputStream, fileSize))
                .build();
        logger.info("Criteria imported successfully");
        return Response.ok(responseModel).build();

    }
}