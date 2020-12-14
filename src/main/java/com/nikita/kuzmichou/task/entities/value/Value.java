package com.nikita.kuzmichou.task.entities.value;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "task_values")
public class Value {
    @Id
    private String name;
    private double value;

    public Value(double value) {
        this.value = value;
    }
}
