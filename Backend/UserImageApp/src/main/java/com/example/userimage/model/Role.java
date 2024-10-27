package com.example.userimage.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@ManyToMany
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<User> users = new HashSet<>();

	public void addUser(User user) {
		users.add(user);
		user.getUserRoles().add(new UserRole(user, this));
	}

	public void removeUser(User user) {
		users.remove(user);
		user.getUserRoles().removeIf(userRole -> userRole.getUser().equals(user) && userRole.getRole().equals(this));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<User> getUsers() {
		return new HashSet<>(users);
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
}
