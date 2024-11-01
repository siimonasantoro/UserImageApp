package com.example.userimage.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Username non può essere vuoto")
	@Size(min = 3, max = 15, message = "Username deve essere lungo tra 3 e 15 caratteri")
	@Column(nullable = false, unique = true)
	private String username;

	@NotBlank(message = "Password non può essere vuoto")
	@Size(min = 8, message = "La password deve essere lunga almeno 8 caratteri")
	@Column(nullable = false)
	private String password;

	@NotBlank(message = "Email non può essere vuota")
	@Email(message = "Email non valida")
	@Column(nullable = false)
	private String email;

	@JsonIgnore
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
	private Set<UserRole> userRoles = new HashSet<>();

	@Column(name = "password_reset_token")
	private String passwordResetToken;

	// Metodi esistenti...

	public List<String> getRoles() {
		return userRoles.stream().map(userRole -> userRole.getRole().getName()).collect(Collectors.toList());
	}

	public String getPasswordResetToken() {
		return passwordResetToken;
	}

	public void setPasswordResetToken(String passwordResetToken) {
		this.passwordResetToken = passwordResetToken;
	}

	public void addRole(Role role) {
		UserRole userRole = new UserRole(this, role);
		userRoles.add(userRole);
		role.getUsers().add(this);
	}

	public void removeRole(Role role) {
		userRoles.removeIf(userRole -> userRole.getRole().equals(role));
		role.getUsers().remove(this);
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<UserRole> getUserRoles() {
		return new HashSet<>(this.userRoles);
	}
}
