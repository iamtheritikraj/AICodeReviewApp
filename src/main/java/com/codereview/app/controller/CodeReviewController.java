package com.codereview.app.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.codereview.app.service.ChatGPTService;
import com.codereview.app.service.GithubService;

import reactor.core.publisher.Mono;



@RestController("/repos/{owner}/{repo}/contents/{path}")
public class CodeReviewController {
	@Autowired
	public GithubService githubservice;
	@Autowired
	public ChatGPTService chatgptservice;

 
    @GetMapping("/repos/{owner}/{repo}/contents/{path}/review-code")
    public Mono<String> reviewCode(OAuth2AuthenticationToken authentication,
            @PathVariable String owner,
            @PathVariable String repo,
            @PathVariable String path) {
    		return githubservice.getFileContent(authentication, owner, repo, path)
    							.flatMap(response -> {
    		String code = response.getBody(); // Extract the code from the response
    		return Mono.just(chatgptservice.getFeedback(code)); // Call ChatGPTService and return the feedback
    		});
}

    	
}

