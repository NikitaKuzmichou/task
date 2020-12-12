package com.nikita.kuzmichou.task.service.calculations;

import com.nikita.kuzmichou.task.entities.value.Value;
import org.springframework.stereotype.Service;

@Service
public class CalculationService {

    public double makeSum(Value first, Value second) {
        return first.getValue() + second.getValue();
    }
}
