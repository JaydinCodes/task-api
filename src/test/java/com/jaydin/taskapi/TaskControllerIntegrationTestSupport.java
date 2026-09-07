package com.jaydin.taskapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

final class TaskControllerIntegrationTestSupport {
    private TaskControllerIntegrationTestSupport() { }
    static int idFrom(String json) throws Exception {
        JsonNode node = new ObjectMapper().readTree(json);
        return node.get("id").asInt();
    }
}
