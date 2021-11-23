package ru.geekbrains.controller.exception;

public class EntityNotFoundException extends RuntimeException {

    private Long id;

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(Long id, String message) {
        super(message);
        this.id = id;
    }

    @Override
    public String getMessage() {
        return super.getMessage().concat(String.format(". Entity id='%d'", id));
    }
}
