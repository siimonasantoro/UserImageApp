package com.example.userimage.controller;


import java.util.HashMap;
import java.util.Map;

import com.example.userimage.dto.ResponseDTO;

public class BaseController {
	public ResponseDTO setResponse(Map<String, Object> content, Boolean status, String message, String error) {
		return new ResponseDTO(content, status, message, error);
	}

	public ResponseDTO setResponse(Boolean status) {
		return setResponse(null, status, "", "");
	}
	
	public ResponseDTO setResponse(Boolean status, String error) {
		return setResponse(null, status, "", error);
	}

	public ResponseDTO setResponse(Map<String, Object> content) {
		return setResponse(content, true);
	}

	public ResponseDTO setResponse(Map<String, Object> content, Boolean status) {
		return setResponse(content, status, "", "");
	}

	public ResponseDTO setResponse(String key, Object value) {
		Map<String, Object> content = new HashMap<>();
		content.put(key, value);
		return setResponse(content, true, "", "");
	}
}
