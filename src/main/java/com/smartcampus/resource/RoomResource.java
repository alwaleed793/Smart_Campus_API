package com.smartcampus.resource;

import com.smartcampus.model.Room;
import com.smartcampus.service.DataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {


    @GET
    public Collection<Room> getAllRooms() {
        return DataStore.rooms.values();
    }


    @GET
    @Path("/{id}")
    public Response getRoomById(@PathParam("id") String id) {

        Room room = DataStore.rooms.get(id);

        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Room not found\"}")
                    .build();
        }

        return Response.ok(room).build();
    }


    @POST
    public Response createRoom(Room room) {

        if (room.getRoomId() == null || room.getRoomId().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Room ID required\"}")
                    .build();
        }

        if (DataStore.rooms.containsKey(room.getRoomId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\":\"Room already exists\"}")
                    .build();
        }

        DataStore.rooms.put(room.getRoomId(), room);

        return Response.status(Response.Status.CREATED)
                .entity(room)
                .build();
    }


    @DELETE
    @Path("/{id}")
    public Response deleteRoom(@PathParam("id") String id) {

        Room room = DataStore.rooms.get(id);

        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Room not found\"}")
                    .build();
        }


        if (!room.getSensors().isEmpty()) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\":\"Room has active sensors\"}")
                    .build();
        }

        DataStore.rooms.remove(id);

        return Response.ok("{\"message\":\"Room deleted successfully\"}")
                .build();
    }
}