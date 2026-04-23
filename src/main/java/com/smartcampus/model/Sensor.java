package com.smartcampus.model;

public class Sensor {

    private String sensorId;
    private String sensorType;
    private String sensorStatus;
    private double latestValue;
    private String linkedRoomId;

    public Sensor() {}

    public Sensor(String sensorId, String sensorType, String sensorStatus, String linkedRoomId) {
        this.sensorId = sensorId;
        this.sensorType = sensorType;
        this.sensorStatus = sensorStatus;
        this.linkedRoomId = linkedRoomId;
    }

    public String getSensorId() { return sensorId; }
    public void setSensorId(String sensorId) { this.sensorId = sensorId; }

    public String getSensorType() { return sensorType; }
    public void setSensorType(String sensorType) { this.sensorType = sensorType; }

    public String getSensorStatus() { return sensorStatus; }
    public void setSensorStatus(String sensorStatus) { this.sensorStatus = sensorStatus; }

    public double getLatestValue() { return latestValue; }
    public void setLatestValue(double latestValue) { this.latestValue = latestValue; }

    public String getLinkedRoomId() { return linkedRoomId; }
    public void setLinkedRoomId(String linkedRoomId) { this.linkedRoomId = linkedRoomId; }
}