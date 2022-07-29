package ru.geekbrains.persist;

public enum OrderStatus {

    CREATED ("Created"),
    MODIFIED ("Modified"),
    ACCESSED ("Accessed"),
    PICKING ("Picking"),
    IN_DELIVERY ("In delivery"),
    DELIVERED ("Delivered"),
    COMPLETED ("Completed");

    private String name;

    OrderStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
