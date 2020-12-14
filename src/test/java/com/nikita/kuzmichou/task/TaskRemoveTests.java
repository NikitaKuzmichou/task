package com.nikita.kuzmichou.task;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nikita.kuzmichou.task.entities.value.Value;
import com.nikita.kuzmichou.task.entities.value.ValueService;
import com.nikita.kuzmichou.task.service.codes.Code;
import com.nikita.kuzmichou.task.service.codes.CodeStatus;
import com.nikita.kuzmichou.task.service.codes.CodesService;
import org.junit.jupiter.api.BeforeAll;
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
public class TaskRemoveTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CodesService codesService;
    @Autowired
    private ValueService valueService;
    @Autowired
    private static Gson gson;

    @BeforeAll
    static void initGson() {
        gson = new GsonBuilder()
                .addSerializationExclusionStrategy(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        if (f.getDeclaredClass() == Value.class &&
                                f.getName().equals("value")) {
                            return true;
                        }
                        return false;
                    }
                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .create();
    }

    @Test
    @DisplayName("Test remove correct value")
    void removeCorrectValue() throws Exception {
        Code respCode = this.codesService.getCodeByCodeStatus(CodeStatus.OK);
        Value removeMe = new Value("remove test", 1.0);
        this.valueService.saveValue(removeMe);
        this.mockMvc.perform(
                post("/remove")
                        .content(gson.toJson(removeMe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(
                        jsonPath("$.code",
                                is(respCode.getCode().toString())))
                .andExpect(
                        jsonPath("$.description",
                                is(respCode.getDescription())));
    }

    @Test
    @DisplayName("Test remove by undefined name")
    void removeByUndefinedName() throws Exception {
        Code respCode = this.codesService.getCodeByCodeStatus(CodeStatus.NOT_FOUND);
        Value removeMe = new Value();
        this.mockMvc.perform(
                post("/remove")
                        .content(gson.toJson(removeMe))
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

    @Test
    @DisplayName("Test remove not existing value")
    void removeNotExistingValue() throws Exception {
        Code respCode = this.codesService.getCodeByCodeStatus(CodeStatus.NOT_FOUND);
        Value removeMe = new Value();
        removeMe.setName("HEHE I'M NOT EXISTING");
        this.mockMvc.perform(
                post("/remove")
                        .content(gson.toJson(removeMe))
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
