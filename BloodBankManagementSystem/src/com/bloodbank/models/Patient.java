package com.bloodbank.models;

public class Patient {
    private int patientId;
    private String name;
    private int age;
    private String gender;
    private String contact;
    private String disease;
    private String bloodGroupRequired;

    public Patient() {}

    public Patient(String name, int age, String gender, String contact, String disease, String bloodGroupRequired) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.contact = contact;
        this.disease = disease;
        this.bloodGroupRequired = bloodGroupRequired;
    }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getDisease() { return disease; }
    public void setDisease(String disease) { this.disease = disease; }

    public String getBloodGroupRequired() { return bloodGroupRequired; }
    public void setBloodGroupRequired(String bloodGroupRequired) { this.bloodGroupRequired = bloodGroupRequired; }

    @Override
    public String toString() {
        return "Patient [patientId=" + patientId + ", name=" + name + ", age=" + age + "]";
    }
}
