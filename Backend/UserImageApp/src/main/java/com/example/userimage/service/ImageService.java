package com.example.userimage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.userimage.model.Image;
import com.example.userimage.repository.ImageRepository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ImageService {

	@Autowired
	private ImageRepository imageRepository;

	public List<Image> getImagesByUserId(Long userId) {
		return imageRepository.findByUserId(userId);
	}

	@Transactional
	public Image saveImage(Image image) {
		return imageRepository.save(image);
	}

	public Optional<Image> findImageById(Long id) {
		return imageRepository.findById(id);
	}

	public void deleteImage(Long id) {
		imageRepository.deleteById(id);
	}

	public byte[] getImageData(Long id) {
		Optional<Image> imageOptional = findImageById(id);
		return imageOptional.map(Image::getData).orElse(null);
	}

	public void saveImageToFileSystem(Long id, String filePath) {
		byte[] imageData = getImageData(id);
		if (imageData != null) {
			try (FileOutputStream fos = new FileOutputStream(filePath)) {
				fos.write(imageData);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
		}
	}
}
