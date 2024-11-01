package com.example.userimage.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.userimage.model.User;
import com.example.userimage.model.UserRole; 
import com.google.gson.Gson;

public class ResponseDTO {
	private Boolean status;
	private String message;
	private String error;
	private String token;
	private Map<String, Object> content;

	public ResponseDTO() {
		this.status = false;
		this.message = "";
		this.error = "";
		this.token = null;
		this.content = new HashMap<>();
	}

	public ResponseDTO(String message, User user, String token) {
		this.status = true;
		this.message = message;
		this.error = "";
		this.token = token;
		this.content = new HashMap<>();

		if (user != null) {
			this.content.put("userId", user.getId());
			this.content.put("userRole", user.getUserRoles().stream().map(userRole -> userRole.getRole().getName())
					.collect(Collectors.toSet()));
		} else {
			this.content.put("userId", null);
			this.content.put("userRole", null);
		}
	}

	public ResponseDTO(String message, User user) {
		this.status = true;
		this.message = message;
		this.error = "";
		this.token = null;
		this.content = new HashMap<>();

		if (user != null) {
			this.content.put("userId", user.getId());
			this.content.put("userRole", user.getUserRoles().stream().map(userRole -> userRole.getRole().getName())
					.collect(Collectors.toSet()));
		} else {
			this.content.put("userId", null);
			this.content.put("userRole", null);
		}
	}

	public ResponseDTO(String token) {
		this();
		this.token = token;
	}

	public ResponseDTO(Boolean status, String message, String error) {
		this.status = status;
		this.message = message;
		this.error = error;
		this.token = null;
		this.content = new HashMap<>();
	}

	public ResponseDTO(String message, String token) {
		this();
		this.message = message;
		this.token = token;
		this.status = true;
		this.error = "";
	}

	public ResponseDTO(Map<String, Object> content, Boolean status, String message, String error) {
		this.content = content != null ? content : new HashMap<>();
		this.status = status;
		this.message = message;
		this.error = error;
		this.token = null;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, Object> getContent() {
		return content;
	}

	public void setContent(Map<String, Object> content) {
		this.content = content != null ? content : new HashMap<>();
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String toJson() {
		return new Gson().toJson(this);

	}

	@Override
	public String toString() {
		return "ResponseDTO{" + "status=" + status + ", message='" + message + '\'' + ", error='" + error + '\''
				+ ", token='" + token + '\'' + ", content=" + content + '}';
	}

	public static void setRoles(Set<UserRole> userRoles) {

	}
}
