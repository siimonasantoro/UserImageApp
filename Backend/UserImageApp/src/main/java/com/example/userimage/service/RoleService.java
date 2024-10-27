package com.example.userimage.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.userimage.model.Role;
import com.example.userimage.repository.RoleRepository;

@Service
public class RoleService {

	@Autowired
	private RoleRepository roleRepository;

	public List<Role> findAllRoles() {
		return roleRepository.findAll();
	}

	public Optional<Role> findRoleById(Long id) {
		return roleRepository.findById(id);
	}

	public Role saveRole(Role role) {
		return roleRepository.save(role);
	}

	public void deleteRole(Long id) {
		roleRepository.deleteById(id);
	}

}
