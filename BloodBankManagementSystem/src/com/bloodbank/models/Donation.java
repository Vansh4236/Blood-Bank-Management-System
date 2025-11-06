package com.bloodbank.models;

public class Donation {
    private int donationId;
    private int donorId;
    private int staffId;
    private int bloodUnitId;
    private String donationDate;
    private String hemoglobinLevel;
    private String bloodPressure;
    private String status;
    private String notes;

    public Donation() {}

    public Donation(int donorId, int staffId, int bloodUnitId, String donationDate) {
        this.donorId = donorId;
        this.staffId = staffId;
        this.bloodUnitId = bloodUnitId;
        this.donationDate = donationDate;
        this.status = "Pending";
    }

    public int getDonationId() { return donationId; }
    public void setDonationId(int donationId) { this.donationId = donationId; }

    public int getDonorId() { return donorId; }
    public void setDonorId(int donorId) { this.donorId = donorId; }

    public int getStaffId() { return staffId; }
    public void setStaffId(int staffId) { this.staffId = staffId; }

    public int getBloodUnitId() { return bloodUnitId; }
    public void setBloodUnitId(int bloodUnitId) { this.bloodUnitId = bloodUnitId; }

    public String getDonationDate() { return donationDate; }
    public void setDonationDate(String donationDate) { this.donationDate = donationDate; }

    public String getHemoglobinLevel() { return hemoglobinLevel; }
    public void setHemoglobinLevel(String hemoglobinLevel) { this.hemoglobinLevel = hemoglobinLevel; }

    public String getBloodPressure() { return bloodPressure; }
    public void setBloodPressure(String bloodPressure) { this.bloodPressure = bloodPressure; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        return "Donation [id=" + donationId + ", status=" + status + "]";
    }
}
