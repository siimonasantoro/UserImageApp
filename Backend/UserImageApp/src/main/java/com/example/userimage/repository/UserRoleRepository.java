package com.example.userimage.repository;

import com.example.userimage.model.User;
import com.example.userimage.model.UserRole;
import com.example.userimage.model.UserRoleId;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {

	 Optional<UserRole> findByUser(User user);
	
	
}
