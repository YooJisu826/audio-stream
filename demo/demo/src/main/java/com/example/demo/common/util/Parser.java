package com.example.demo.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Parser {

    public static String extractMessage(String jsonString, String key) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            return jsonNode.get("message").asText();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return jsonString;
    }
}
