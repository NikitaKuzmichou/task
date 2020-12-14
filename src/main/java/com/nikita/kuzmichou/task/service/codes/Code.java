package com.nikita.kuzmichou.task.service.codes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Code {
    private CodeStatus code;
    private String description;
}
