package com.example.userimage.controller;

import com.example.userimage.model.Image;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.userimage.model.User;
import com.example.userimage.service.ImageService;
import com.example.userimage.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/images")
public class ImageController {

	@Autowired
	private UserService userService;

	@Autowired
	private ImageService imageService;

	@GetMapping
	public ResponseEntity<List<Image>> getImagesForLoggedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Long userId = userService.getUserIdByUsername(userDetails.getUsername());

		List<Image> userImages = imageService.getImagesByUserId(userId);
		return ResponseEntity.ok(userImages);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Image> getImageById(@PathVariable Long id) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Long userId = userService.getUserIdByUsername(userDetails.getUsername());

		Optional<Image> imageOptional = imageService.findImageById(id);
		if (imageOptional.isPresent()) {
			Image image = imageOptional.get();
			if (!image.getUser().getId().equals(userId)) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Accesso negato
			}
			return ResponseEntity.ok(image);
		} else {
			return ResponseEntity.notFound().build();
		}
		
	}
	
	@GetMapping("/users")
	public ResponseEntity<List<User>> getAllUsers() {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication == null || !authentication.isAuthenticated()) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }

	    if (authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
	    }

	    List<User> users = userService.findAllUsers();
	    return ResponseEntity.ok(users);
	}

	@GetMapping("/users/{userId}/images")
	public ResponseEntity<List<Image>> getImagesByUserId(@PathVariable Long userId) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication == null || !authentication.isAuthenticated()) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }

	    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	    Long currentUserId = userService.getUserIdByUsername(userDetails.getUsername());

	    boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
	    if (!isAdmin && !currentUserId.equals(userId)) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); 
	    }

	    List<Image> userImages = imageService.getImagesByUserId(userId);
	    return ResponseEntity.ok(userImages);
	}


	@GetMapping("/{id}/data")
	public ResponseEntity<byte[]> getImageData(@PathVariable Long id) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Long userId = userService.getUserIdByUsername(userDetails.getUsername());

		Optional<Image> imageOptional = imageService.findImageById(id);
		if (imageOptional.isPresent()) {
			Image image = imageOptional.get();
			if (!image.getUser().getId().equals(userId)) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); 
			}
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", image.getContentType());
			return new ResponseEntity<>(image.getData(), headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/upload")
	public ResponseEntity<Image> uploadUserImage(@RequestParam("image") MultipartFile file) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Long userId = userService.getUserIdByUsername(userDetails.getUsername());


		try {
			Image image = new Image();
			image.setFilename(file.getOriginalFilename());
			image.setContentType(file.getContentType());
			image.setData(file.getBytes());

			User user = userService.findUserById(userId).orElseThrow(() -> new RuntimeException("User not found"));
			image.setUser(user);

			Image createdImage = imageService.saveImage(image);


			String filePath = "src/main/resources/images/" + createdImage.getId() + "_" + image.getFilename();
			imageService.saveImageToFileSystem(createdImage.getId(), filePath);

			return ResponseEntity.status(HttpStatus.CREATED).body(createdImage);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}


	@PutMapping("/{id}")
	public ResponseEntity<Image> updateImage(@PathVariable Long id, @RequestBody Image image) {
		if (!imageService.findImageById(id).isPresent()) {
			return ResponseEntity.notFound().build();
		}
		image.setId(id);
		Image updatedImage = imageService.saveImage(image);
		return ResponseEntity.ok(updatedImage);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
		if (!imageService.findImageById(id).isPresent()) {
			return ResponseEntity.notFound().build();
		}
		imageService.deleteImage(id);
		return ResponseEntity.noContent().build();
	}
}
