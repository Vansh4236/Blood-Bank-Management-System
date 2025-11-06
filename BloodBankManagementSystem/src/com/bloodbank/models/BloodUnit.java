package com.bloodbank.models;

public class BloodUnit {
    private int bloodUnitId;
    private int groupId;
    private String bloodType;
    private int volumeMl;
    private String collectionDate;
    private String expiryDate;
    private String status;

    public BloodUnit() {}

    public BloodUnit(int groupId, int volumeMl, String expiryDate) {
        this.groupId = groupId;
        this.volumeMl = volumeMl;
        this.expiryDate = expiryDate;
        this.status = "Available";
    }

    public int getBloodUnitId() { return bloodUnitId; }
    public void setBloodUnitId(int bloodUnitId) { this.bloodUnitId = bloodUnitId; }

    public int getGroupId() { return groupId; }
    public void setGroupId(int groupId) { this.groupId = groupId; }

    public String getBloodType() { return bloodType; }
    public void setBloodType(String bloodType) { this.bloodType = bloodType; }

    public int getVolumeMl() { return volumeMl; }
    public void setVolumeMl(int volumeMl) { this.volumeMl = volumeMl; }

    public String getCollectionDate() { return collectionDate; }
    public void setCollectionDate(String collectionDate) { this.collectionDate = collectionDate; }

    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "BloodUnit [unitId=" + bloodUnitId + ", type=" + bloodType + ", status=" + status + "]";
    }
}
