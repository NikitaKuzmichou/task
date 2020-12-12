package com.nikita.kuzmichou.task.entities.value.exceptions;

public class AlreadyStoredException extends RuntimeException {

    public AlreadyStoredException() {
        super();
    }

    public AlreadyStoredException(String details) {
        super(details);
    }
}
