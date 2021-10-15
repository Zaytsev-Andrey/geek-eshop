package ru.geekbrains.exception;

public class OrderDetailNotFoundException extends RuntimeException {

    public OrderDetailNotFoundException(Long id) {
        super(String.format("Order detail with id='%d' not found", id));
    }
}
