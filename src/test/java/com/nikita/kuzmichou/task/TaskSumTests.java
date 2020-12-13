package com.nikita.kuzmichou.task;

import com.nikita.kuzmichou.task.entities.value.Value;
import com.nikita.kuzmichou.task.entities.value.ValueService;
import com.nikita.kuzmichou.task.service.codes.Code;
import com.nikita.kuzmichou.task.service.codes.CodeStatus;
import com.nikita.kuzmichou.task.service.codes.CodesService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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

    @Test
    @DisplayName("Test both values names existing")
    void sumTwoCorrect() throws Exception {
        try {
            Value first = new Value("first remove test", 1);
            Value second = new Value("second remove test", 1);
            this.valueService.saveValue(first);
            this.valueService.saveValue(second);
            Code respCode = this.codesService.getCodeByCodeStatus(CodeStatus.OK);
            StringBuilder sb = new StringBuilder();
            sb.append("{\"first\":\"")
              .append(first.getName())
              .append("\",\"second\":\"")
              .append(second.getName())
              .append("\"}");
            this.mockMvc.perform(
                    post("/sum")
                            .content(sb.toString())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString(
                            "{" +
                                    "\"code\":\"" + respCode.getCode() + "\"," +
                                    "\"description\":\"" +
                                               respCode.getDescription() + "\"," +
                                    "\"sum\":" + 2.0 +
                                    "}")));
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
            StringBuilder sb = new StringBuilder();
            sb.append("{\"first\":\"")
                    .append(first.getName())
                    .append("\",\"second\":\"")
                    .append(second.getName())
                    .append("\"}");
            this.mockMvc.perform(
                    post("/sum")
                            .content(sb.toString())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(content().string(containsString(
                            "{" +
                                    "\"code\":\"" + respCode.getCode() + "\"," +
                                    "\"description\":\"" +
                                    respCode.getDescription() + "\"" +
                                    "}")));
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
            StringBuilder sb = new StringBuilder();
            sb.append("{\"first\":\"")
                    .append(first.getName())
                    .append("\",\"second\":\"")
                    .append(second.getName())
                    .append("\"}");
            this.mockMvc.perform(
                    post("/sum")
                            .content(sb.toString())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(content().string(containsString(
                            "{" +
                                    "\"code\":\"" + respCode.getCode() + "\"," +
                                    "\"description\":\"" +
                                              respCode.getDescription() + "\"" +
                                    "}")));
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
        StringBuilder sb = new StringBuilder();
        sb.append("{\"first\":\"")
                .append(first.getName())
                .append("\",\"second\":\"")
                .append(second.getName())
                .append("\"}");
        this.mockMvc.perform(
                post("/sum")
                        .content(sb.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(
                        "{" +
                                "\"code\":\"" + respCode.getCode() + "\"," +
                                "\"description\":\"" +
                                          respCode.getDescription() + "\"" +
                                "}")));
    }
}
