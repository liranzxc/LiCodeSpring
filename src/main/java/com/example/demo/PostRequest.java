package com.example.demo;

import java.util.List;

public class PostRequest {
	
	private String email;
	private String password;
	private String url ;
	private String text;
	private List<String> groupsname;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<String> getGroupsname() {
		return groupsname;
	}
	public void setGroupsname(List<String> groupsname) {
		this.groupsname = groupsname;
	}
	
	
	

}
