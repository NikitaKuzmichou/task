package com.nikita.kuzmichou.task.entities.value.exceptions;

public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super();
    }

    public NotFoundException(String details) {
        super(details);
    }
}
