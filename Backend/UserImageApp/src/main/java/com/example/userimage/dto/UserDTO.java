package com.example.userimage.dto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.userimage.model.UserRole;

public class UserDTO {
	private Long id;
	private String username;
	private String email;
	private List<String> roles; 

	
	public UserDTO(Long id, String username, String email, Set<UserRole> userRoles) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.roles = userRoles.stream().map(userRole -> userRole.getRole().getName()).collect(Collectors.toList());
	}

	public UserDTO(Long id, String username, String email, List<String> roles) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.roles = roles;
	}

	
	public UserDTO(Long id, String username, String email, String role) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.roles = List.of(role);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
}
