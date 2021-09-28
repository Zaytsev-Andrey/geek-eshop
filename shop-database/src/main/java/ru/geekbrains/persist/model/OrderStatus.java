package ru.geekbrains.persist.model;

public enum OrderStatus {

    CREATED ("Created"),
    MODIFIED ("Modified"),
    COMPLETED ("Completed");

    private String name;

    OrderStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
