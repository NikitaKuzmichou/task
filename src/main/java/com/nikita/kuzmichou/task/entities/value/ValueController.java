package com.nikita.kuzmichou.task.entities.value;

import com.nikita.kuzmichou.task.entities.value.dto.ValueDto;
import com.nikita.kuzmichou.task.entities.value.dto.ValueMapper;
import com.nikita.kuzmichou.task.service.codes.Code;
import com.nikita.kuzmichou.task.service.codes.CodeStatus;
import com.nikita.kuzmichou.task.service.codes.CodesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;

@RestController
@RequestMapping("/")
public class ValueController {
    @Autowired
    private ValueService valueService;
    @Autowired
    private ValueMapper valueMapper;
    @Autowired
    private CodesService codesService;

    @PostMapping("/add")
    public ResponseEntity<ModelAndView> addValue(@RequestBody ValueDto valueDto) {
        Value saved = this.valueService.saveValue(this.valueMapper.toValue(valueDto));
        Code respCode = this.codesService.getCodeByCodeStatus(CodeStatus.OK);
        /**TODO EXCEPTIONS & CODES GENERATION*/
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("code", respCode.getCode());
        modelAndView.addObject("description", respCode.getDescription());
        return new ResponseEntity<>(modelAndView, respCode.getStatus());
    }

    @PostMapping("/remove")
    public ResponseEntity<ModelAndView> removeValue(@RequestBody String name) {
        ModelAndView modelAndView = new ModelAndView();
        /**TODO EXCEPTIONS(???) & CODES GENERATION*/
        //modelAndView.addObject("code", "");
        //modelAndView.addObject("description", "");
        this.valueService.deleteValue(name);
        return new ResponseEntity<>(modelAndView, HttpStatus.ACCEPTED);
    }

    @PostMapping("/sum")
    public ResponseEntity<ModelAndView> sumValues(@RequestBody String first,
                                                  @RequestBody String second) {
        ModelAndView modelAndView = new ModelAndView();
        //modelAndView.addObject("sum", "");
        //modelAndView.addObject("code", "");
        //modelAndView.addObject("description", "");
        return new ResponseEntity<>(modelAndView, HttpStatus.OK);
    }
}
