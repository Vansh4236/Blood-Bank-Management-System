package com.bloodbank.models;

public class Staff {
    private int staffId;
    private String name;
    private String role;
    private String contactNo;
    private String shiftTime;
    private String email;

    public Staff() {}

    public Staff(String name, String role, String contactNo, String shiftTime, String email) {
        this.name = name;
        this.role = role;
        this.contactNo = contactNo;
        this.shiftTime = shiftTime;
        this.email = email;
    }

    public int getStaffId() { return staffId; }
    public void setStaffId(int staffId) { this.staffId = staffId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getContactNo() { return contactNo; }
    public void setContactNo(String contactNo) { this.contactNo = contactNo; }

    public String getShiftTime() { return shiftTime; }
    public void setShiftTime(String shiftTime) { this.shiftTime = shiftTime; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "Staff [id=" + staffId + ", name=" + name + ", role=" + role + "]";
    }
}
