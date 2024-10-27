package com.example.userimage.model;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class UserRoleId implements Serializable {
	 private static final long serialVersionUID = 1L;
	 
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "role_id")
	private Long roleId;

	public UserRoleId() {
	}

	public UserRoleId(Long userId, Long roleId) {
		this.userId = userId;
		this.roleId = roleId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		UserRoleId that = (UserRoleId) o;

		if (!userId.equals(that.userId))
			return false;
		return roleId.equals(that.roleId);
	}

	@Override
	public int hashCode() {
		int result = userId.hashCode();
		result = 31 * result + roleId.hashCode();
		return result;
	}
}
