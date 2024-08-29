package com.codereview.app.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class CodeReviewController {

 

    @GetMapping("/review-code")
    public String reviewCode(@RequestParam String repoName, @RequestParam String filePath) {
    	return null;
    }
}

