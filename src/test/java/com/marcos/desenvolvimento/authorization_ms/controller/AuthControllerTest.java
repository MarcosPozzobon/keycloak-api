package com.marcos.desenvolvimento.authorization_ms.controller;

import com.marcos.desenvolvimento.authorization_ms.dto.request.LoginRequest;
import com.marcos.desenvolvimento.authorization_ms.dto.response.TokenResponseDTO;
import com.marcos.desenvolvimento.authorization_ms.service.LoginService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest {

    private MockMvc mockMvc;
    private LoginService loginService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        loginService = mock(LoginService.class);
        AuthController authController = new AuthController(loginService);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void loginDeveRetornarTokenComStatus200() throws Exception {
        LoginRequest request = new LoginRequest("user", "pass");
        TokenResponseDTO tokenResponse = new TokenResponseDTO("access123", "refresh123", 3600);

        when(loginService.obterToken(request)).thenReturn(tokenResponse);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("access123"))
                .andExpect(jsonPath("$.refreshToken").value("refresh123"))
                .andExpect(jsonPath("$.expiresIn").value(3600));

        verify(loginService, times(1)).obterToken(request);
    }
}