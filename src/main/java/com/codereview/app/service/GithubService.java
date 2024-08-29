package com.codereview.app.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.List;
@Component
public class GithubService {

    @Autowired
    private OAuth2AuthorizedClientService clientService;

    @Autowired
    private WebClient.Builder webClientBuilder;

    private WebClient createWebClient(String token) {
        return webClientBuilder
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
    }
    public Mono<ResponseEntity<List<String>>> getRepos(OAuth2AuthenticationToken authentication) {
        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(
            authentication.getAuthorizedClientRegistrationId(),
            authentication.getName());

        String token = client.getAccessToken().getTokenValue();

        WebClient webClient = createWebClient(token);
        return webClient.get()
                .uri("https://api.github.com/user/repos")
                .retrieve()
                .bodyToFlux(JsonNode.class) // Deserialize directly to a Flux<JsonNode>
                .map(repoNode -> repoNode.get("full_name").asText()) // Extract full_name from each node
                .collectList() // Collect into a list
                .map(fullNames -> ResponseEntity.ok(fullNames))
                .doOnError(e -> System.err.println("Error: " + e.getMessage())); // Improved logging
    }

    public Mono<ResponseEntity<String>> getFileContent(
            OAuth2AuthenticationToken authentication,
            @PathVariable String owner,
            @PathVariable String repo,
            @PathVariable String path) {
        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(
            authentication.getAuthorizedClientRegistrationId(),
            authentication.getName());

        String token = client.getAccessToken().getTokenValue();

        WebClient webClient = createWebClient(token);
        return webClient.get()
                .uri(String.format("https://api.github.com/repos/%s/%s/contents/%s", owner, repo, path))
                .retrieve()
                .bodyToMono(String.class)
                .map(responseBody -> {
                    System.out.println("Raw Response Body: " + responseBody); // Log raw response for debugging
                    return decodeBase64Content(responseBody);
                })
                .map(content -> ResponseEntity.ok(content))
                .doOnError(e -> System.err.println("Error: " + e.getMessage())); // Improved logging
    }

    private String decodeBase64Content(String responseBody) {
        try {
            // Parse JSON response
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(responseBody);
            
            // Extract base64 encoded content
            String encodedContent = jsonNode.get("content").asText();
            
            // Sanitize base64 content (remove unexpected characters or padding)
            encodedContent = sanitizeBase64(encodedContent);
            
            // Decode base64 content
            byte[] decodedBytes = Base64.getDecoder().decode(encodedContent);
            
            // Convert bytes to string
            return new String(decodedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error decoding content: " + e.getMessage(); // Include exception message in response
        }
    }

    private String sanitizeBase64(String value) {
        // Replace any non-base64 characters and ensure proper padding
        // Base64 strings should be padded to the correct length, so adding padding if necessary
        String sanitized = value.trim().replaceAll("[^A-Za-z0-9+/=]", "");
        int padding = sanitized.length() % 4;
        if (padding > 0) {
            sanitized += "=".repeat(4 - padding);
        }
        return sanitized;
    }
}
