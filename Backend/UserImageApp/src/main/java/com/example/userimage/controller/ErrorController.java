package com.example.userimage.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.userimage.dto.ResponseDTO;

@RestController
@RequestMapping("api/public/status")
public class ErrorController extends BaseController {

	@GetMapping("/401")
	public ResponseDTO error401() {
		return setResponse(false, "401");
	}
}
