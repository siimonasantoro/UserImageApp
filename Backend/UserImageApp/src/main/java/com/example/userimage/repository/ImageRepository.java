package com.example.userimage.repository;

import com.example.userimage.model.Image;
import com.example.userimage.model.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
	List<Image> findByUserId(Long userId);

	List<Image> findByUser(User user);

}