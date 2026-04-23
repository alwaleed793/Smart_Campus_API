package com.smartcampus.service;

import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStore {

    // store rooms
    public static Map<String, Room> rooms = new HashMap<>();

    // store sensors
    public static Map<String, Sensor> sensors = new HashMap<>();

    // store readings for each sensor
    public static Map<String, List<SensorReading>> sensorReadings = new HashMap<>();
}