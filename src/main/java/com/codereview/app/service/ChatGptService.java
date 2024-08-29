package com.codereview.app.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatGPTService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getFeedback(String code) {
        try {
            // Create the JSON payload using ObjectMapper
            ObjectNode requestBody = objectMapper.createObjectNode();
            requestBody.put("model", "gpt-4o-mini"); // Ensure this model name is correct
            ArrayNode messagesArray = objectMapper.createArrayNode();
            ObjectNode message = objectMapper.createObjectNode();
            message.put("role", "user");
            message.put("content", "Review this code: " + code);
            messagesArray.add(message);
            requestBody.set("messages", messagesArray);

            // Convert requestBody to JSON string
            String payload = objectMapper.writeValueAsString(requestBody);

            // Set up headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiKey);
            headers.set("Content-Type", "application/json");

            // Create the HTTP entity with headers and body
            HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);

            // Send the request and get the response
            ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                // Parse and return the response
                JsonNode responseBody = objectMapper.readTree(responseEntity.getBody());
                return responseBody
                        .path("choices")
                        .get(0)
                        .path("message")
                        .path("content")
                        .asText();
            } else {
                // Handle unsuccessful response
                return "Error: Unable to get feedback from OpenAI API. Status Code: " + responseEntity.getStatusCode();
            }
        } catch (Exception e) {
            // Handle exceptions
            e.printStackTrace();
            return "Error: An exception occurred while calling the OpenAI API. " + e.getMessage();
        }
    }
}
