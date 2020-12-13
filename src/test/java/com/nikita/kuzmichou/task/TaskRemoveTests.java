package com.nikita.kuzmichou.task;

import com.nikita.kuzmichou.task.entities.value.Value;
import com.nikita.kuzmichou.task.entities.value.ValueService;
import com.nikita.kuzmichou.task.service.codes.Code;
import com.nikita.kuzmichou.task.service.codes.CodeStatus;
import com.nikita.kuzmichou.task.service.codes.CodesService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import static org.hamcrest.Matchers.containsString;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskRemoveTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CodesService codesService;
    @Autowired
    private ValueService valueService;

    @Test
    @DisplayName("Test remove correct value")
    void removeCorrectValue() throws Exception {
        Code respCode = this.codesService.getCodeByCodeStatus(CodeStatus.OK);
        Value testData = new Value("remove test", 1.0);
        this.mockMvc.perform(
                post("/remove")
                        .content(testData.getName())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(content().string(containsString(
                        "{" +
                                "\"code\":\"" + respCode.getCode() + "\"," +
                                "\"description\":\"" +
                                          respCode.getDescription() + "\"" +
                                "}")));
    }

    @Test
    @DisplayName("Test remove by undefined name")
    void removeByUndefinedName() throws Exception {
        Code respCode = this.codesService.getCodeByCodeStatus(CodeStatus.NOT_FOUND);
        this.mockMvc.perform(
                post("/remove")
                        .content("")
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

    @Test
    @DisplayName("Test remove not existing value")
    void removeNotExistingValue() throws Exception {
        Code respCode = this.codesService.getCodeByCodeStatus(CodeStatus.NOT_FOUND);
        this.mockMvc.perform(
                post("/remove")
                        .content("HEHE I'M NOT EXISTING")
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
