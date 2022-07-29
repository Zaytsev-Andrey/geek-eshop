package ru.geekbrains.exception;

import java.util.UUID;

public class OrderDetailNotFoundException extends RuntimeException {

    public OrderDetailNotFoundException(UUID id) {
        super(String.format("Order detail with id='%s' not found", id));
    }
}
