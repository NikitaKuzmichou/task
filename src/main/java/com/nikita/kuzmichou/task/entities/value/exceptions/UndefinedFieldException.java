package com.nikita.kuzmichou.task.entities.value.exceptions;

public class UndefinedFieldException extends RuntimeException {

    public UndefinedFieldException() {
        super();
    }

    public UndefinedFieldException(String details) {
        super(details);
    }
}
