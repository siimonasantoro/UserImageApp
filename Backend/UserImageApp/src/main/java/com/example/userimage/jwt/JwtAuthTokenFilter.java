package com.example.userimage.jwt;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.userimage.model.User;
import com.example.userimage.model.UserMain;
import com.example.userimage.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthTokenFilter extends OncePerRequestFilter {

	@Autowired
	private JwtProvider tokenProvider;

	@Autowired
	private UserService userDetailsService;

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthTokenFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String jwt = getJwt(request);
		if (jwt != null) {
			try {
				if (tokenProvider.validateJwtToken(jwt)) {
					String username = tokenProvider.getUserNameFromJwtToken(jwt);

					User user = userDetailsService.findByUsername(username);
					if (user != null) {
						UserMain userDetails = UserMain.build(user, username);

						List<GrantedAuthority> authorities = user.getUserRoles().stream()
								.map(role -> new SimpleGrantedAuthority(role.getRole().getName()))
								.collect(Collectors.toList());

						UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
								userDetails, null, authorities);
						authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(authentication);
						logger.info("Authenticated user: {}", userDetails.getUsername());
					} else {
						logger.warn("User not found with username: {}", username);
					}
				} else {
					logger.warn("Invalid JWT token");
				}
			} catch (Exception e) {
				logger.error("Could not set user authentication -> Message: {}", e.getMessage());
			}
		}
		filterChain.doFilter(request, response);
	}

	private String getJwt(HttpServletRequest request) {
		String ret = null;
		String authHeader = request.getHeader("Authorization");
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			ret = authHeader.replace("Bearer ", "");
		}
		return ret;
	}
}
