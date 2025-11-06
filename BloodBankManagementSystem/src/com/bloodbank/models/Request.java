package com.bloodbank.models;

public class Request {
    private int requestId;
    private int patientId;
    private int hospitalId;
    private int groupId;
    private int unitsRequested;
    private String requestDate;
    private String status;
    private String urgency;
    private String notes;

    public Request() {}

    public Request(int patientId, int hospitalId, int groupId, int unitsRequested, String requestDate) {
        this.patientId = patientId;
        this.hospitalId = hospitalId;
        this.groupId = groupId;
        this.unitsRequested = unitsRequested;
        this.requestDate = requestDate;
        this.status = "Pending";
        this.urgency = "Medium";
    }

    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public int getHospitalId() { return hospitalId; }
    public void setHospitalId(int hospitalId) { this.hospitalId = hospitalId; }

    public int getGroupId() { return groupId; }
    public void setGroupId(int groupId) { this.groupId = groupId; }

    public int getUnitsRequested() { return unitsRequested; }
    public void setUnitsRequested(int unitsRequested) { this.unitsRequested = unitsRequested; }

    public String getRequestDate() { return requestDate; }
    public void setRequestDate(String requestDate) { this.requestDate = requestDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getUrgency() { return urgency; }
    public void setUrgency(String urgency) { this.urgency = urgency; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        return "Request [id=" + requestId + ", status=" + status + ", urgency=" + urgency + "]";
    }
}
