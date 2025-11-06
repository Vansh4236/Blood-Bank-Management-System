package com.bloodbank.models;

public class BloodGroup {
    private int groupId;
    private String bloodType;
    private int availableUnits;

    public BloodGroup() {}

    public BloodGroup(int groupId, String bloodType, int availableUnits) {
        this.groupId = groupId;
        this.bloodType = bloodType;
        this.availableUnits = availableUnits;
    }

    public int getGroupId() { return groupId; }
    public void setGroupId(int groupId) { this.groupId = groupId; }

    public String getBloodType() { return bloodType; }
    public void setBloodType(String bloodType) { this.bloodType = bloodType; }

    public int getAvailableUnits() { return availableUnits; }
    public void setAvailableUnits(int availableUnits) { this.availableUnits = availableUnits; }

    @Override
    public String toString() {
        return bloodType;
    }
}
