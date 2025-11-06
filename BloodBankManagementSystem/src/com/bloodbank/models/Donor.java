package com.bloodbank.models;

public class Donor {
    private int donorId;
    private String name;
    private int age;
    private String gender;
    private String contact;
    private int bloodGroupId;
    private String bloodType;
    private String email;
    private String address;
    private String lastDonationDate;

    // Empty Constructor
    public Donor() {}

    // Constructor with parameters
    public Donor(String name, int age, String gender, String contact, 
                 int bloodGroupId, String email, String address) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.contact = contact;
        this.bloodGroupId = bloodGroupId;
        this.email = email;
        this.address = address;
    }

    // Getters and Setters
    public int getDonorId() { return donorId; }
    public void setDonorId(int donorId) { this.donorId = donorId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public int getBloodGroupId() { return bloodGroupId; }
    public void setBloodGroupId(int bloodGroupId) { this.bloodGroupId = bloodGroupId; }

    public String getBloodType() { return bloodType; }
    public void setBloodType(String bloodType) { this.bloodType = bloodType; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getLastDonationDate() { return lastDonationDate; }
    public void setLastDonationDate(String lastDonationDate) { this.lastDonationDate = lastDonationDate; }

    @Override
    public String toString() {
        return "Donor [donorId=" + donorId + ", name=" + name + ", age=" + age + 
               ", bloodType=" + bloodType + ", contact=" + contact + "]";
    }
}
