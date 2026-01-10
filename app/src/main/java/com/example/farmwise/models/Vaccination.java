package com.example.farmwise.models;

import com.google.firebase.Timestamp;

public class Vaccination {
    private String id;
    private String animalId;
    private String vaccineName;
    private Timestamp dateGiven;
    private Timestamp nextDueDate;
    private String notes;
    private String status;
    private Timestamp createdAt;

    public Vaccination() {
    }

    public Vaccination(String animalId, String vaccineName, Timestamp dateGiven, Timestamp nextDueDate, String notes,
            String status, Timestamp createdAt) {
        this.animalId = animalId;
        this.vaccineName = vaccineName;
        this.dateGiven = dateGiven;
        this.nextDueDate = nextDueDate;
        this.notes = notes;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnimalId() {
        return animalId;
    }

    public void setAnimalId(String animalId) {
        this.animalId = animalId;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public Timestamp getDateGiven() {
        return dateGiven;
    }

    public void setDateGiven(Timestamp dateGiven) {
        this.dateGiven = dateGiven;
    }

    public Timestamp getNextDueDate() {
        return nextDueDate;
    }

    public void setNextDueDate(Timestamp nextDueDate) {
        this.nextDueDate = nextDueDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
