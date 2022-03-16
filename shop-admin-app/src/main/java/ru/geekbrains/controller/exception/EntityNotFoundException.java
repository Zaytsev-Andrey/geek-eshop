package ru.geekbrains.controller.exception;

public class EntityNotFoundException extends RuntimeException {

    private String id;

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String id, String message) {
        super(message);
        this.id = id;
    }

    @Override
    public String getMessage() {
        return super.getMessage().concat(String.format(". Entity id='%s'", id));
    }
}
