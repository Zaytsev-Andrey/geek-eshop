package ru.geekbrains.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(String.format("User with login='%s' not found", message));
    }
}
