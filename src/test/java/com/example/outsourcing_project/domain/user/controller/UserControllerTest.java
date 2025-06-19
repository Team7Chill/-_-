package com.example.outsourcing_project.domain.user.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("회원 탈퇴")
    void withdraw() throws Exception {
        // Given
        String withdrawUrl = "/api/auth/withdraw";

        // When
        ResultActions actual = mockMvc.perform(MockMvcRequestBuilders.post(withdrawUrl));

        // Then
        actual.andDo(MockMvcResultHandlers.print());
    }
}
