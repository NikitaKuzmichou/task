package com.nikita.kuzmichou.task.entities.value;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "task_values")
public class Value {
    @Id
    private String name;
    private double value;

    public Value() {

    }

    public Value(String name) {
        this.name = name;
    }

    public Value(String name, double value) {
        this.name = name;
        this.value = value;
    }

    public Value(double value) {
        this.value = value;
    }
}
