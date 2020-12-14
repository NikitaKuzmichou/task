package com.nikita.kuzmichou.task;

import com.nikita.kuzmichou.task.entities.value.Value;
import com.nikita.kuzmichou.task.entities.value.ValueService;
import com.nikita.kuzmichou.task.service.codes.Code;
import com.nikita.kuzmichou.task.service.codes.CodeStatus;
import com.nikita.kuzmichou.task.service.codes.CodesService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskSumTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CodesService codesService;
    @Autowired
    private ValueService valueService;

    private String getJsonStr(Value first, Value second) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("first", first.getName());
        jsonObject.put("second", second.getName());
        return jsonObject.toString();
    }

    @Test
    @DisplayName("Test both values names existing")
    void sumTwoCorrect() throws Exception {
        try {
            Value first = new Value("first remove test", 1);
            Value second = new Value("second remove test", 1);
            this.valueService.saveValue(first);
            this.valueService.saveValue(second);
            Code respCode = this.codesService.getCodeByCodeStatus(CodeStatus.OK);
            this.mockMvc.perform(
                    post("/sum")
                            .content(this.getJsonStr(first, second))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(
                            jsonPath("$.code",
                                    is(respCode.getCode().toString())))
                    .andExpect(
                            jsonPath("$.description",
                                    is(respCode.getDescription())))
                    .andExpect(
                            jsonPath("$.sum",
                                    is(first.getValue() + second.getValue())));
        } finally {
            this.valueService.deleteValue("first remove test");
            this.valueService.deleteValue("second remove test");
        }
    }


    @Test
    @DisplayName("Test first value name exist and second not")
    void sumSndIncorrect() throws Exception {
        try {
            Value first = new Value("first remove test", 1);
            Value second = new Value("second remove test", 1);
            this.valueService.saveValue(first);
            Code respCode = this.codesService.getCodeByCodeStatus(CodeStatus.NOT_FOUND);
            this.mockMvc.perform(
                    post("/sum")
                            .content(this.getJsonStr(first, second))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(
                            jsonPath("$.code",
                                    is(respCode.getCode().toString())))
                    .andExpect(
                            jsonPath("$.description",
                                    is(respCode.getDescription())));
        } finally {
            this.valueService.deleteValue("first remove test");
        }
    }

    @Test
    @DisplayName("Test second value name exist and first not")
    void sumFstIncorrect() throws Exception {
        try {
            Value first = new Value("first remove test", 1);
            Value second = new Value("second remove test", 1);
            this.valueService.saveValue(second);
            Code respCode = this.codesService.getCodeByCodeStatus(CodeStatus.NOT_FOUND);
            this.mockMvc.perform(
                    post("/sum")
                            .content(this.getJsonStr(first, second))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(
                            jsonPath("$.code",
                                    is(respCode.getCode().toString())))
                    .andExpect(
                            jsonPath("$.description",
                                    is(respCode.getDescription())));
        } finally {
            this.valueService.deleteValue("second remove test");
        }
    }

    @Test
    @DisplayName("Test both values incorrect")
    void sumBothIncorrect() throws Exception {
        Value first = new Value("first remove test", 1);
        Value second = new Value("second remove test", 1);
        Code respCode = this.codesService.getCodeByCodeStatus(CodeStatus.NOT_FOUND);
        this.mockMvc.perform(
                post("/sum")
                        .content(this.getJsonStr(first, second))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.code",
                                is(respCode.getCode().toString())))
                .andExpect(
                        jsonPath("$.description",
                                is(respCode.getDescription())));
    }
}
