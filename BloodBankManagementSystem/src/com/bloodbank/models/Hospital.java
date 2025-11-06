package com.bloodbank.models;

public class Hospital {
    private int hospitalId;
    private String name;
    private String address;
    private String contactNo;
    private String email;
    private String city;

    public Hospital() {}

    public Hospital(String name, String address, String contactNo, String email, String city) {
        this.name = name;
        this.address = address;
        this.contactNo = contactNo;
        this.email = email;
        this.city = city;
    }

    public int getHospitalId() { return hospitalId; }
    public void setHospitalId(int hospitalId) { this.hospitalId = hospitalId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getContactNo() { return contactNo; }
    public void setContactNo(String contactNo) { this.contactNo = contactNo; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    @Override
    public String toString() {
        return "Hospital [id=" + hospitalId + ", name=" + name + ", city=" + city + "]";
    }
}
