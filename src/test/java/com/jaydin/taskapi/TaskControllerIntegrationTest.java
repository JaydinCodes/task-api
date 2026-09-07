package com.jaydin.taskapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TaskControllerIntegrationTest {
    @Autowired MockMvc mockMvc;

    @Test
    void everyEndpointHasTheExpectedContract() throws Exception {
        String created = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Write tests\",\"description\":\"Initial\"}"))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.completed").value(false)).andReturn().getResponse().getContentAsString();
        int id = com.jaydin.taskapi.TaskControllerIntegrationTestSupport.idFrom(created);

        mockMvc.perform(get("/api/tasks")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(id));
        mockMvc.perform(get("/api/tasks/{id}", id)).andExpect(status().isOk()).andExpect(jsonPath("$.title").value("Write tests"));
        mockMvc.perform(put("/api/tasks/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated\",\"description\":\"Changed\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(id)).andExpect(jsonPath("$.title").value("Updated"));
        mockMvc.perform(patch("/api/tasks/{id}/complete", id)).andExpect(status().isOk()).andExpect(jsonPath("$.completed").value(true));
        mockMvc.perform(put("/api/tasks/{id}/replace", id).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Replacement\",\"description\":\"New\",\"completed\":false}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(id)).andExpect(jsonPath("$.title").value("Replacement")).andExpect(jsonPath("$.completed").value(false));
        mockMvc.perform(delete("/api/tasks/{id}", id)).andExpect(status().isNoContent());
        mockMvc.perform(get("/api/tasks/{id}", id)).andExpect(status().isNotFound()).andExpect(jsonPath("$.title").value("Task not found"));
    }

    @Test
    void invalidAndMissingRequestsReturnClientErrors() throws Exception {
        mockMvc.perform(post("/api/tasks").contentType(MediaType.APPLICATION_JSON).content("{\"title\":\" \"}"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors.title").exists());
        mockMvc.perform(delete("/api/tasks/999")).andExpect(status().isNotFound());
    }
}
