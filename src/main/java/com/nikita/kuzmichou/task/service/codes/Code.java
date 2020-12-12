package com.nikita.kuzmichou.task.service.codes;

import lombok.Data;

@Data
public class Code {
    private CodeStatus code;
    private String description;

    public Code() {

    }

    public Code(CodeStatus code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public String toString() {
        return "\"code\":" + this.code + ",\"description\":" + this.description;
    }
}
