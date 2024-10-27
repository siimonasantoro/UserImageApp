package com.example.userimage.security;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import com.example.userimage.dto.ResponseDTO;
import com.example.userimage.jwt.JwtAuthEntryPoint;
import com.example.userimage.jwt.JwtAuthTokenFilter;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
	@Autowired
	AuthenticationConfiguration authenticationConfiguration;
	@Autowired
	private JwtAuthEntryPoint unauthorizedHandler;
//	@Autowired
//	private KeycloakLogoutHandler keycloakLogoutHandler;

	@Bean
	JwtAuthTokenFilter authenticationJwtTokenFilter() {
		return new JwtAuthTokenFilter();
	}

	@Bean
	AuthenticationManager authenticationManager() throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

//	@Order(1)
	@Bean
	SecurityFilterChain configure(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
	    MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
	    http.cors(withDefaults()) 
	        .csrf(csrf -> csrf.disable())
	        .exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandler))
	        .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	        .authorizeHttpRequests(requests -> {
	            requests.requestMatchers(mvcMatcherBuilder.pattern("/api/public/**")).permitAll()
	                .requestMatchers(mvcMatcherBuilder.pattern("/images/upload")).hasRole("USER")
	                .requestMatchers(mvcMatcherBuilder.pattern("/api/admin/**")).hasAuthority("ROLE_ADMIN")
	                .requestMatchers(mvcMatcherBuilder.pattern("/images/**"))
	                .hasAnyAuthority("ROLE_ADMIN", "ROLE_USER") 
	                .requestMatchers(mvcMatcherBuilder.pattern("/images/{id}/data"))
	                .hasAnyAuthority("ROLE_ADMIN", "ROLE_USER") 
	                .requestMatchers(mvcMatcherBuilder.pattern("/api/public/status")).permitAll()
	                .requestMatchers(mvcMatcherBuilder.pattern("/app/**")).permitAll()
	                .requestMatchers(mvcMatcherBuilder.pattern("/api/public/sign-in")).permitAll()
	                .requestMatchers(mvcMatcherBuilder.pattern("/api/public/sign-up")).permitAll();
	        })
	        .logout(logout -> logout.permitAll().logoutSuccessHandler((request, response, authentication) -> {
	            response.setStatus(HttpServletResponse.SC_OK);
	            response.getWriter().write(new ResponseDTO().toJson());
	        }));
	    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	    return http.build();
	}
}