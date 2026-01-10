package com.example.farmwise.models;

import com.google.firebase.Timestamp;

public class FarmProfile {
    private String farmName;
    private String city;
    private String country;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public FarmProfile() {
    }

    public FarmProfile(String farmName, String city, String country, Timestamp createdAt, Timestamp updatedAt) {
        this.farmName = farmName;
        this.city = city;
        this.country = country;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
