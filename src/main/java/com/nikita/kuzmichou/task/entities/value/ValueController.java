package com.nikita.kuzmichou.task.entities.value;

import com.nikita.kuzmichou.task.entities.value.dto.ValueDto;
import com.nikita.kuzmichou.task.entities.value.dto.ValueMapper;
import com.nikita.kuzmichou.task.entities.value.exceptions.AlreadyStoredException;
import com.nikita.kuzmichou.task.entities.value.exceptions.NotFoundException;
import com.nikita.kuzmichou.task.entities.value.exceptions.UndefinedFieldException;
import com.nikita.kuzmichou.task.service.calculations.CalculationService;
import com.nikita.kuzmichou.task.service.codes.Code;
import com.nikita.kuzmichou.task.service.codes.CodeStatus;
import com.nikita.kuzmichou.task.service.codes.CodesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
    private CalculationService calculationService;

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
    public ResponseEntity<ModelAndView> addValue(@RequestBody ValueDto valueDto) {
        if (Objects.isNull(valueDto.getName()) ||
                valueDto.getName().isEmpty() ||
                Double.isNaN(valueDto.getValue())) {
            Code errorCode = this.codesService.getCodeByCodeStatus(
                                                   CodeStatus.FIELD_UNDEFINED);
            throw new UndefinedFieldException(errorCode.toString());
        } else if (this.valueService.getValue(valueDto.getName()).isPresent()) {
            Code errorCode = this.codesService.getCodeByCodeStatus(
                                                    CodeStatus.ALREADY_STORED);
            throw new AlreadyStoredException(errorCode.toString());
        }
        this.valueService.saveValue(this.valueMapper.toValue(valueDto));
        return new ResponseEntity<>(this.getOkModelAndView(), HttpStatus.CREATED);
    }

    @PostMapping("/remove")
    public ResponseEntity<ModelAndView> removeValue(@RequestBody String name) {
        /**TODO EXCEPTIONS
         * TODO TEST WHAT IF DELETE NOT EXISTING ELEMENT*/
        this.valueService.deleteValue(name);
        return new ResponseEntity<>(this.getOkModelAndView(), HttpStatus.ACCEPTED);
    }

    @PostMapping("/sum")
    public ResponseEntity<ModelAndView> sumValues(@RequestBody String first,
                                                  @RequestBody String second) {
        Value firstVal = this.valueService.getValue(first).orElseThrow(() -> {
            Code errorCode = this.codesService.getCodeByCodeStatus(
                                                         CodeStatus.NOT_FOUND);
            return new NotFoundException(errorCode.toString());
        });
        Value secondVal = this.valueService.getValue(second).orElseThrow(() -> {
            Code errorCode = this.codesService.getCodeByCodeStatus(
                                                         CodeStatus.NOT_FOUND);
            return new NotFoundException(errorCode.toString());
        });
        ModelAndView modelAndView = this.getOkModelAndView();
        modelAndView.addObject("sum",
                         this.calculationService.makeSum(firstVal, secondVal));
        return new ResponseEntity<>(modelAndView, HttpStatus.OK);
    }

    private ModelAndView getOkModelAndView() {
        Code respCode = this.codesService.getCodeByCodeStatus(CodeStatus.OK);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("code", respCode.getCode());
        modelAndView.addObject("description", respCode.getDescription());
        return modelAndView;
    }
}
