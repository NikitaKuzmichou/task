package com.nikita.kuzmichou.task.entities.value;

import com.google.gson.Gson;
import com.nikita.kuzmichou.task.entities.value.dto.ValueDto;
import com.nikita.kuzmichou.task.entities.value.dto.ValueMapper;
import com.nikita.kuzmichou.task.entities.value.exceptions.AlreadyStoredException;
import com.nikita.kuzmichou.task.entities.value.exceptions.NotFoundException;
import com.nikita.kuzmichou.task.entities.value.exceptions.UndefinedFieldException;
import com.nikita.kuzmichou.task.service.sum.SumService;
import com.nikita.kuzmichou.task.service.codes.Code;
import com.nikita.kuzmichou.task.service.codes.CodeStatus;
import com.nikita.kuzmichou.task.service.codes.CodesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/")
public class ValueController {
    @Autowired
    private ValueService valueService;
    @Autowired
    private CodesService codesService;
    @Autowired
    private ValueMapper valueMapper;
    @Autowired
    private SumService sumService;
    @Autowired
    private Gson gson;

    @ExceptionHandler({AlreadyStoredException.class,
                       NotFoundException.class,
                       UndefinedFieldException.class})
    public ResponseEntity<String> handleException(final Exception ex) {
        ResponseEntity<String> response;
        if (Objects.equals(ex.getClass(), NotFoundException.class)) {
            response = new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } else {
            response = new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addValue(@RequestBody ValueDto valueDto) {
        if (Objects.isNull(valueDto.getName()) ||
                valueDto.getName().isEmpty() ||
                Double.isNaN(valueDto.getValue())) {
            Code errorCode = this.codesService.getCodeByCodeStatus(
                                                   CodeStatus.FIELD_UNDEFINED);
            throw new UndefinedFieldException(this.gson.toJson(errorCode));
        } else if (this.valueService.getValue(valueDto.getName()).isPresent()) {
            Code errorCode = this.codesService.getCodeByCodeStatus(
                                                    CodeStatus.ALREADY_STORED);
            throw new AlreadyStoredException(this.gson.toJson(errorCode));
        }
        this.valueService.saveValue(this.valueMapper.toValue(valueDto));
        return new ResponseEntity<>(this.okResponse(), HttpStatus.CREATED);
    }

    @PostMapping("/remove")
    public ResponseEntity<Map<String, Object>> removeValue(@RequestBody String name) {
        this.valueService.getValue(name).orElseThrow(() -> {
            Code errorCode = this.codesService.getCodeByCodeStatus(
                                                         CodeStatus.NOT_FOUND);
            return new NotFoundException(this.gson.toJson(errorCode));
        });
        this.valueService.deleteValue(name);
        return new ResponseEntity<>(this.okResponse(), HttpStatus.ACCEPTED);
    }

    @PostMapping("/sum")
    public ResponseEntity<Map<String, Object>> sumValues(
                                                  @RequestBody String values) {
        Map<String, String> vals = this.sumService.getValuesNames(values);
        Value firstVal = this.valueService.getValue(vals.get("first"))
                                                           .orElseThrow(() -> {
            Code errorCode = this.codesService.getCodeByCodeStatus(
                                                         CodeStatus.NOT_FOUND);
            return new NotFoundException(this.gson.toJson(errorCode));
        });
        Value secondVal = this.valueService.getValue(vals.get("second"))
                                                           .orElseThrow(() -> {
            Code errorCode = this.codesService.getCodeByCodeStatus(
                                                         CodeStatus.NOT_FOUND);
            return new NotFoundException(this.gson.toJson(errorCode));
        });
        Map<String, Object> response = this.okResponse();
        response.put("sum",
                         this.sumService.makeSum(firstVal, secondVal));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private Map<String, Object> okResponse() {
        Code respCode = this.codesService.getCodeByCodeStatus(CodeStatus.OK);
        Map<String, Object> response = new HashMap<>();
        response.put("code", respCode.getCode().toString());
        response.put("description", respCode.getDescription());
        return response;
    }
}
