package com.example.userimage.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.userimage.dto.ImageDTO;
import com.example.userimage.dto.SignUpDTO;
import com.example.userimage.dto.UserDTO;
import com.example.userimage.model.Image;
import com.example.userimage.model.Role;
import com.example.userimage.model.User;
import com.example.userimage.model.UserMain;
import com.example.userimage.model.UserRole;
import com.example.userimage.repository.ImageRepository;
import com.example.userimage.repository.RoleRepository;
import com.example.userimage.repository.UserRepository;
import com.example.userimage.repository.UserRoleRepository;
import com.example.userimage.exception.ResourceNotFoundException; // Devi creare questa eccezione

import jakarta.transaction.Transactional;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserRoleRepository userRoleRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private ImageRepository imageRepository;

	public List<User> findAllUsers() {
		return userRepository.findAll();
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public Optional<User> findUserById(Long id) {
		return userRepository.findById(id);
	}

	public String createPasswordResetTokenForUser(User user) {
		String token = UUID.randomUUID().toString();
		user.setPasswordResetToken(token);
		userRepository.save(user);
		return token;
	}

	public void updatePassword(User user, String newPassword) {
		user.setPassword(passwordEncoder.encode(newPassword));
		user.setPasswordResetToken(null);
		userRepository.save(user);
	}

	public User saveUser(User user) {
		return userRepository.save(user);
	}

	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}

	public User updateUser(Long id, User user) {
		if (userRepository.existsById(id)) {
			user.setId(id);
			return userRepository.save(user);
		} else {
			throw new RuntimeException("User not found");
		}
	}

	public User findByPasswordResetToken(String token) {
		return userRepository.findByPasswordResetToken(token);
	}

	public User findByUsername(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User not found with username: " + username));
	}

	public UserDetails loadUser(Map<String, Object> user) {
		return UserMain.build(user);
	}

	public User authenticate(String username, String password) {
		Optional<User> userOptional = userRepository.findByUsername(username);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			if (passwordEncoder.matches(password, user.getPassword())) {
				return user;
			} else {
				System.err.println("Password non corrisponde");
			}
		} else {
			System.err.println("Utente non trovato");
		}
		return null;
	}

	public Long getUserIdByUsername(String username) {
		Optional<User> userOptional = userRepository.findByUsername(username);
		return userOptional.map(User::getId).orElse(null);
	}

	public List<UserDTO> getAllUsers() {
		List<User> users = userRepository.findAll();
		return users.stream()
				.map(user -> new UserDTO(user.getId(), user.getUsername(), user.getEmail(), getUserRole(user)))
				.collect(Collectors.toList());
	}

	private String getUserRole(User user) {
		return userRoleRepository.findByUser(user).map(userRole -> userRole.getRole().getName())
				.orElseThrow(() -> new RuntimeException("Nessun ruolo trovato per l'utente"));
	}

	public List<ImageDTO> getUserImages(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Utente non trovato con ID: " + userId));

		List<Image> images = imageRepository.findByUser(user);
		return images.stream().map(image -> new ImageDTO(image.getId(), image.getFilename(), generateImageUrl(image)))
				.collect(Collectors.toList());
	}

	private String generateImageUrl(Image image) {
		return "data:" + image.getContentType() + ";base64," + image.getData();
	}

	@Transactional
	public User registerUser(SignUpDTO signUpDTO) {
		try {
			User newUser = new User();
			newUser.setUsername(signUpDTO.getUsername());
			newUser.setEmail(signUpDTO.getEmail());
			newUser.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));

			newUser = userRepository.save(newUser);
			System.out.println("New User ID: " + newUser.getId());

			// CREA UTENTE CON RUOLO ROLE_USER, 2L CORRISPONDE AD ID 2 NEL DB
			Role userRole = roleRepository.findById(2L)
					.orElseThrow(() -> new RuntimeException("Role not found for ID: 2"));
			UserRole userRoleAssociation = new UserRole(newUser, userRole);

			userRoleRepository.save(userRoleAssociation);

			return newUser;
		} catch (Exception e) {
			throw new RuntimeException("Failed to register user: " + e.getMessage());
		}
	}
}
