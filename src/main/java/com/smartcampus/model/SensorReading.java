package com.smartcampus.model;

public class SensorReading {

    private String readingId;
    private long timeRecorded;
    private double readingValue;

    public SensorReading() {}

    public SensorReading(String readingId, long timeRecorded, double readingValue) {
        this.readingId = readingId;
        this.timeRecorded = timeRecorded;
        this.readingValue = readingValue;
    }

    public String getReadingId() { return readingId; }
    public void setReadingId(String readingId) { this.readingId = readingId; }

    public long getTimeRecorded() { return timeRecorded; }
    public void setTimeRecorded(long timeRecorded) { this.timeRecorded = timeRecorded; }

    public double getReadingValue() { return readingValue; }
    public void setReadingValue(double readingValue) { this.readingValue = readingValue; }
}