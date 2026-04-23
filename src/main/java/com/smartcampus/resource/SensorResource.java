package com.smartcampus.resource;

import com.smartcampus.model.Sensor;
import com.smartcampus.model.Room;
import com.smartcampus.service.DataStore;
import com.smartcampus.exception.LinkedResourceNotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.stream.Collectors;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    @POST
    public Response addNewSensor(Sensor sensor) {

        Room room = DataStore.rooms.get(sensor.getLinkedRoomId());

        if (room == null) {
            throw new
    LinkedResourceNotFoundException("Room not found");
        }

        if (DataStore.sensors.containsKey(sensor.getSensorId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\":\"Sensor already exists\"}")
                    .build();
        }

        DataStore.sensors.put(sensor.getSensorId(), sensor);
        room.getSensors().add(sensor.getSensorId());

        return Response.status(Response.Status.CREATED)
                .entity(sensor)
                .build();
    }

    @GET
    public Collection<Sensor> fetchSensors(@QueryParam("type") String type) {

        if (type == null || type.isEmpty()) {
            return DataStore.sensors.values();
        }

        return DataStore.sensors.values()
                .stream()
                .filter(s -> type.equalsIgnoreCase(s.getSensorType()))
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public Response getSingleSensor(@PathParam("id") String id) {

        Sensor sensor = DataStore.sensors.get(id);

        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Sensor not found\"}")
                    .build();
        }

        return Response.ok(sensor).build();
    }

    @Path("/{id}/readings")
    public SensorReadingResource getSensorReadings(@PathParam("id") String id) {
        return new SensorReadingResource(id);
    }
}