package com.nikita.kuzmichou.task.service.codes;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class Code {
    private CodeStatus code;
    private String description;
    private HttpStatus status;

    public Code() {

    }

    public Code(CodeStatus code, String description, HttpStatus status) {
        this.code = code;
        this.description = description;
        this.status = status;
    }
}
