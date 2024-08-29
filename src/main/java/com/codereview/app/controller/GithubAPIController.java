package com.codereview.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.codereview.app.service.GithubService;

import reactor.core.publisher.Mono;

@RestController
public class GithubAPIController {
    @Autowired
    public GithubService githubservice;

	
	@GetMapping("/repos")
	public Mono<ResponseEntity<List<String>>> getUserRepos(OAuth2AuthenticationToken authentication)
	{
		return githubservice.getRepos(authentication);

	}
    @GetMapping("/repos/{owner}/{repo}/contents/{path}")
    public Mono<ResponseEntity<String>> getRepoFileContent(
            OAuth2AuthenticationToken authentication,
            @PathVariable String owner,
            @PathVariable String repo,
            @PathVariable String path)
    {
    	return githubservice.getFileContent(authentication, owner, repo, path);
    }
}
