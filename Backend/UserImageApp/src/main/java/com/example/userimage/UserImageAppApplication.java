package com.example.userimage;

import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class UserImageAppApplication {
	
	@Autowired
	private Environment env;

	public static void main(String[] args) {
		SpringApplication.run(UserImageAppApplication.class, args);
	}

	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(UserImageAppApplication.class);
	}

	@Bean
	WebMvcConfigurer corsConfigurer() {
	    return new WebMvcConfigurer() {
	        @Override
	        public void addCorsMappings(CorsRegistry registry) {
	            registry.addMapping("/api/**") 
	                .allowedOrigins("http://localhost:4200") 
	                .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
	                .allowCredentials(true)
	                .maxAge(3600);
	        }
	    };
	}


	@Bean
	TimeZone setDefaultTimeZone() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		return TimeZone.getTimeZone("UTC");
	}

}
