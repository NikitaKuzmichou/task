package com.nikita.kuzmichou.task.entities;

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
}
