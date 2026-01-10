package com.example.farmwise.models;

import com.google.firebase.Timestamp;

public class Livestock {
    private String id;
    private String type;
    private String name;
    private String breed;
    private int age;
    private String health;
    private Timestamp createdAt;

    public Livestock() {
    }

    public Livestock(String type, String name, String breed, int age, String health, Timestamp createdAt) {
        this.type = type;
        this.name = name;
        this.breed = breed;
        this.age = age;
        this.health = health;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getHealth() {
        return health;
    }

    public void setHealth(String health) {
        this.health = health;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return name;
    }
}
