package ru.diasoft.micro.demo.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.io.IOException;
import java.net.URI;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import ru.diasoft.micro.demo.repository.AutoModel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonInclude;

@SpringBootTest
@AutoConfigureMockMvc
class AutoModelControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void defaultMethodTest() throws Exception {

		this.mockMvc.perform(get("/automodel")).andDo(print()).andExpect(status().isOk());
	}

	@Test
	void autoModelFindByIdTest() throws Exception {

		this.mockMvc.perform(get("/automodel/2"))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.modelId").value("2"));
	}

	@Test
	void autoModelCreateTest() throws Exception {
		
		this.mockMvc.perform(post(new URI("/automodel")).contentType("application/json")
				.content(convertObjectToJsonBytes(getAutoModelEntity())))
				.andDo(print()).andExpect(status().isOk());
	}
	

	@Test
	void autoModelModifyTest() throws Exception {

		this.mockMvc.perform(put(new URI("/automodel/10")).contentType("application/json")
				.content(convertObjectToJsonBytes(getAutoModelEntity())))
				.andDo(print()).andExpect(status().isOk());
		
		this.mockMvc.perform(get("/automodel/10"))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.fullName").value("fullName"));
	}
	

	@Test
	void autoModelDeleteTest() throws Exception {

		this.mockMvc.perform(delete(new URI("/automodel/10")))
				.andDo(print()).andExpect(status().isOk());
		
		this.mockMvc.perform(get("/automodel/10"))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.modelId").doesNotExist());
	}
	
	private AutoModel getAutoModelEntity() {
    	AutoModel obj = AutoModel.builder()
    			.markId(20L)
    			.briefName("briefName")
    			.fullName("fullName")
    			.category("category")
    			.year(2020)
    			.build();
        return obj;
    }
	
	public static byte[] convertObjectToJsonBytes(Object object)
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return mapper.writeValueAsBytes(object);
    }
}

