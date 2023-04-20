package com.misispiclix.singleplayergames.onirim.controller;

import com.misispiclix.singleplayergames.onirim.service.IOnirimService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OnirimRestController.class)
class OnirimRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    @Qualifier(value = "onirimServiceImpl")
    IOnirimService onirimService;

    @Test
    void getExamples() {
    }

    @Test
    void getExampleById() throws Exception {
        mockMvc.perform(get("/onirim/api/example/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is("1")));
    }

    @Test
    void createExample() {
    }

    @Test
    void updateExample() {
    }

    @Test
    void updateExamplePatch() {
    }

    @Test
    void deleteExample() {
    }
}