package com.example.userimage.model;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserMain implements UserDetails {
	private static final long serialVersionUID = 1L;

	@JsonIgnore
	private Long id;
	@JsonIgnore
	private String username;
	@JsonIgnore
	private String email;
	@JsonIgnore
	private String password;
	@JsonIgnore
	private Collection<? extends GrantedAuthority> authorities;
	private Map<String, Object> authUser;

	private static final Logger logger = LoggerFactory.getLogger(UserMain.class);

	public UserMain(Long id, String username, String email, String password,
			Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.authorities = authorities;
	}

	public static UserMain build(User user, String appUsername) {
		List<GrantedAuthority> authorities = user.getUserRoles().stream().map(userRole -> {
			String roleName = userRole.getRole().getName();
			logger.debug("User role found: {}", roleName);
			return new SimpleGrantedAuthority(roleName);
		}).collect(Collectors.toList());
		return new UserMain(user.getId(), appUsername, user.getEmail(), user.getPassword(), authorities);
	}

	@SuppressWarnings("unchecked")
	public static UserMain build(Map<String, Object> userJwt) {
		Map<String, Object> user = (Map<String, Object>) userJwt.get("user");
		List<Object> userRoles = (List<Object>) userJwt.get("userRoles");
		List<GrantedAuthority> authorities = userRoles.stream()
				.map(role -> new SimpleGrantedAuthority(((Map<String, Object>) role).get("userTypeName").toString()))
				.collect(Collectors.toList());
		UserMain userMain = new UserMain((Long) user.get("id"), user.get("username").toString(),
				user.get("email").toString(), null, authorities);
		userMain.setAuthUser(userJwt);
		return userMain;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		UserMain user = (UserMain) o;
		return Objects.equals(id, user.id);
	}

	public Long getId() {
		return id;
	}

	public Map<String, Object> getAuthUser() {
		return authUser;
	}

	public void setAuthUser(Map<String, Object> authUser) {
		this.authUser = authUser;
	}
}
