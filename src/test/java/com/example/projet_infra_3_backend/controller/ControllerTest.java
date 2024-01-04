package com.example.projet_infra_3_backend.controller;

import com.example.projet_infra_3_backend.config.JWTTokenProvider;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(Controller.class)
public class ControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private JWTTokenProvider jwtTokenProvider;

	@Test
	public void getProfile() throws Exception {
		this.mockMvc.perform(get("/user/{username}/profile", "abc"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(jsonPath("$.id").value("<value>"))
			.andExpect(jsonPath("$.firstName").value("<value>"))
			.andExpect(jsonPath("$.lastName").value("<value>"))
			.andExpect(jsonPath("$.username").value("<value>"))
			.andExpect(jsonPath("$.password").value("<value>"))
			.andExpect(jsonPath("$.email").value("<value>"))
			.andExpect(jsonPath("$.profileImageUrl").value("<value>"))
			.andExpect(jsonPath("$.lastLoginDate").value("<value>"))
			.andExpect(jsonPath("$.lastLoginDateDisplay").value("<value>"))
			.andExpect(jsonPath("$.joinDate").value("<value>"))
			.andExpect(jsonPath("$.role").value("<value>"))
			.andExpect(jsonPath("$.authorities[0]").value("<value>"))
			.andExpect(jsonPath("$.isNotLocked").value("<value>"));
	}
}
