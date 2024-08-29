package com.codereview.app.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="CodeReview")
public class FeedbackData{
	private String repo_name;
	private String file_path;
	private int line_no;
	private String issue_type;
	private String suggestion;
	
	
	
	public String getRepo_name() {
		return repo_name;
	}
	public void setRepo_name(String repo_name) {
		this.repo_name = repo_name;
	}
	public String getFile_path() {
		return file_path;
	}
	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}
	public int getLine_no() {
		return line_no;
	}
	public void setLine_no(int line_no) {
		this.line_no = line_no;
	}
	public String getIssue_type() {
		return issue_type;
	}
	public void setIssue_type(String issue_type) {
		this.issue_type = issue_type;
	}
	public String getSuggestion() {
		return suggestion;
	}
	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}
	
	
	@Override
	public String toString() {
		return "Feedback [repo_name=" + repo_name + ", file_path=" + file_path + ", line_no=" + line_no
				+ ", issue_type=" + issue_type + ", suggestion=" + suggestion + "]";
	}
	
	
	

}
