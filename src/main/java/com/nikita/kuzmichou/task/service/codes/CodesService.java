package com.nikita.kuzmichou.task.service.codes;

import org.springframework.stereotype.Service;

@Service
public class CodesService {

    public Code getCodeByCodeStatus(CodeStatus status) {
        switch (status) {
            case OK:
                return new Code(status, "OK");
            case FIELD_UNDEFINED:
                return new Code(status, "Name or value undefined");
            case ALREADY_STORED:
                return new Code(status, "Entity already stored");
            case NOT_FOUND:
                return new Code(status, "Not found");
            default:
                return new Code(status, "Unknown status code");
        }
    }
}
