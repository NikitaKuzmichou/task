package com.nikita.kuzmichou.task;

import com.google.gson.Gson;
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
import static org.hamcrest.Matchers.containsString;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class TaskAddTests {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private Gson gson;
	@Autowired
	private CodesService codesService;
	@Autowired
	private ValueService valueService;

	@Test
	@DisplayName("Test add correct value")
	void addCorrectValue() throws Exception {
		try {
			Value addTest = new Value("addTest", 15.0);
			Code respCode = this.codesService.getCodeByCodeStatus(CodeStatus.OK);
			this.mockMvc.perform(
					post("/add")
							.content(gson.toJson(addTest))
							.contentType(MediaType.APPLICATION_JSON))
					.andDo(print())
					.andExpect(status().isCreated())
					.andExpect(content().string(containsString(
							"{" +
									"\"code\":\"" + respCode.getCode() + "\"," +
									"\"description\":\"" +
									respCode.getDescription() + "\"" +
									"}")));
		} finally {
			this.valueService.deleteValue("addTest");
		}
	}

	@Test
	@DisplayName("Test add already stored value")
	void addAlreadyStoredValue() throws Exception {
		try {
			Value storedTest = new Value("stored", 1.0);
			this.valueService.saveValue(storedTest);
			Code respCode = this.codesService.getCodeByCodeStatus(CodeStatus.ALREADY_STORED);
			this.mockMvc.perform(
					post("/add")
							.content(gson.toJson(storedTest))
							.contentType(MediaType.APPLICATION_JSON))
					.andDo(print())
					.andExpect(status().isBadRequest())
					.andExpect(
							content().string(containsString(
									"{" +
											"\"code\":\"" + respCode.getCode() + "\"," +
											"\"description\":\"" +
											respCode.getDescription() + "\"" +
											"}")));
		} finally {
			this.valueService.deleteValue("stored");
		}
	}

	@Test
	@DisplayName("Test add undefined name")
	void addUndefinedName() throws Exception {
		Code respCode = this.codesService.getCodeByCodeStatus(CodeStatus.FIELD_UNDEFINED);
		this.mockMvc.perform(
				post("/add")
						.content(gson.toJson(new Value(15.0)))
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString(
						"{" +
								"\"code\":\"" + respCode.getCode() + "\"," +
								"\"description\":\"" +
								          respCode.getDescription() + "\"" +
								"}")));
	}

	@Test
	@DisplayName("Test add empty name")
	void addEmptyValue() throws Exception {
		Code respCode = this.codesService.getCodeByCodeStatus(CodeStatus.FIELD_UNDEFINED);
		this.mockMvc.perform(
				post("/add")
						.content(gson.toJson(new Value("", 15.0)))
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString(
						"{" +
								"\"code\":\"" + respCode.getCode() + "\"," +
								"\"description\":\"" +
								          respCode.getDescription() + "\"" +
								"}")));
	}
}
