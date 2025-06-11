package com.nutrition.mx.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuthResponse {
	private String token;

	public AuthResponse(String token) {
		this.token = token;
	}
}
