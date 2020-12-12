package com.nikita.kuzmichou.task.service.codes;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CodesService {

    public Code getCodeByCodeStatus(CodeStatus status) {
        switch (status) {
            case OK:
                return new Code(status, "OK", HttpStatus.OK);
            case NAME_UNDEFINED:
                return new Code(status, "Name undefined", HttpStatus.BAD_REQUEST);
            case VALUE_UNDEFINED:
                return new Code(status, "Value undefined", HttpStatus.BAD_REQUEST);
            case ALREADY_STORED:
                return new Code(status, "Entity already stored", HttpStatus.BAD_REQUEST);
            case NOT_FOUND:
                return new Code(status, "Not found", HttpStatus.NOT_FOUND);
            default:
                return new Code(status, "Unknown status code", HttpStatus.NOT_FOUND);
        }
    }
}
