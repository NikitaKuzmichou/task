package com.nikita.kuzmichou.task.service.sum;

import com.google.gson.Gson;
import com.nikita.kuzmichou.task.entities.value.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SumService {
    @Autowired
    private Gson gson;

    public Map<String, String> getValuesNames(String json) {
        return this.gson.fromJson(json, Map.class);
    }

    public double makeSum(Value first, Value second) {
        return first.getValue() + second.getValue();
    }
}
