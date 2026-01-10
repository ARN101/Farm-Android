package com.example.farmwise.models;

import com.google.firebase.Timestamp;

public class Finance {
    private String id;
    private String type;
    private String category;
    private double amount;
    private Timestamp date;
    private String animalId;
    private String notes;
    private Timestamp createdAt;

    public Finance() {
    }

    public Finance(String type, String category, double amount, Timestamp date, String animalId, String notes,
            Timestamp createdAt) {
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.animalId = animalId;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getAnimalId() {
        return animalId;
    }

    public void setAnimalId(String animalId) {
        this.animalId = animalId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
