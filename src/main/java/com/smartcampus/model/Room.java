package com.smartcampus.model;

import java.util.ArrayList;
import java.util.List;

public class Room {

    private String roomId;
    private String roomName;
    private int maxCapacity;
    private List<String> sensors = new ArrayList<>();

    public Room() {}

    public Room(String roomId, String roomName, int maxCapacity) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.maxCapacity = maxCapacity;
    }

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }

    public int getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }

    public List<String> getSensors() { return sensors; }
}