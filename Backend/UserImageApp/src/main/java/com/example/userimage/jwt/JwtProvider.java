package com.example.userimage.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.example.userimage.model.UserMain;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {

	private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

	@Value("${app.jwtSecret}")
	private String jwtSecret;

	@Value("${app.jwtExpiration}")
	private int jwtExpiration;

	public String generateJwtToken(Authentication authentication) {
	    UserMain userPrincipal = (UserMain) authentication.getPrincipal();
	    SecretKey secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	    
	    Map<String, Object> claims = new HashMap<>();
	    claims.put("roles", userPrincipal.getAuthorities().stream()
	        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

	    return Jwts.builder()
	        .claims(claims)
	        .subject(userPrincipal.getUsername())
	        .issuedAt(new Date())
	        .expiration(new Date((new Date()).getTime() + jwtExpiration * 1000))
	        .signWith(secret)
	        .compact();
	}

	public String generateJwtToken(String appUsername, Map<String, Object> content) {
	    SecretKey secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	    Map<String, Object> claims = (content != null) ? new HashMap<>(content) : new HashMap<>();
	    
	    return Jwts.builder()
	            .claims(claims)
	            .subject(appUsername)
	            .issuedAt(new Date())
	            .expiration(new Date((new Date()).getTime() + jwtExpiration * 1000))
	            .signWith(secret)
	            .compact();
	}

	public boolean validateJwtToken(String authToken) {
		Boolean ret = false;
		try {
			// Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			SecretKey secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
			Jwts.parser().verifyWith(secret).build().parseSignedClaims(authToken).getPayload();
			ret = true;
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token -> Message: {}", e);
		} catch (ExpiredJwtException e) {
			logger.error("Expired JWT token -> Message: {}", e);
		} catch (UnsupportedJwtException e) {
			logger.error("Unsupported JWT token -> Message: {}", e);
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty -> Message: {}", e);
		} catch (Exception e) {
			logger.error("Invalid JWT signature -> Message: {} ", e);
		}
		return ret;
	}

	public String getUserNameFromJwtToken(String token) {
		SecretKey secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
		return Jwts.parser().verifyWith(secret).build().parseSignedClaims(token).getPayload().getSubject();
	}

	public Map<String, Object> getUser(String token) {
		return getUser(token, jwtSecret);
	}

	public Map<String, Object> getUser(String token, String jwtSecret) {
		SecretKey secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
		return (Map<String, Object>) Jwts.parser().verifyWith(secret).build().parseSignedClaims(token).getPayload();
	}
}