package com.example.userimage.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.userimage.dto.ResponseDTO;

@RestController
@RequestMapping("/api/authenticated")
public class AuthenticatedController {

	@GetMapping("/test")
	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('SuperUser')")
	public ResponseEntity<ResponseDTO> test() {
		new ResponseEntity<ResponseDTO>(HttpStatus.OK);
		return ResponseEntity.ok(new ResponseDTO("test", "test"));
	}
}
