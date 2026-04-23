package com.smartcampus.exception.mapper;

import com.smartcampus.exception.*;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable ex) {

        if (ex instanceof SensorUnavailableException) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"error\":\"" + ex.getMessage() + "\"}")
                    .build();
        }

        if (ex instanceof LinkedResourceNotFoundException) {
            return Response.status(422)
                    .entity("{\"error\":\"" + ex.getMessage() + "\"}")
                    .build();
        }

        if (ex instanceof RoomNotEmptyException) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\":\"" + ex.getMessage() + "\"}")
                    .build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"error\":\"Internal Server Error\"}")
                .build();
    }
}