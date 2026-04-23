package com.smartcampus.resource;

import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;
import com.smartcampus.service.DataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }


    @GET
    public List<SensorReading> getAllReadings() {

        Sensor sensor = DataStore.sensors.get(sensorId);

        if (sensor == null) {
            return new ArrayList<>();
        }

        return DataStore.sensorReadings
                .getOrDefault(sensorId, new ArrayList<>());
    }


    @POST
    public Response createReading(SensorReading reading) {

        Sensor sensor = DataStore.sensors.get(sensorId);

        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Sensor not found")
                    .build();
        }


        if ("MAINTENANCE".equalsIgnoreCase(sensor.getSensorStatus())) {
            throw new com.smartcampus.exception.SensorUnavailableException("Sensor under maintenance");

        }

        DataStore.sensorReadings
                .computeIfAbsent(sensorId, k -> new ArrayList<>())
                .add(reading);

        // update latest value
        sensor.setLatestValue(reading.getReadingValue());

        return Response.status(Response.Status.CREATED)
                .entity(reading)
                .build();
    }
}