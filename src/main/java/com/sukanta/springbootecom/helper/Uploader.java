package com.sukanta.springbootecom.helper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Component
public class Uploader {
	private final String UPLOAD_DIR =
			System.getProperty("user.dir") + "/src/main/resources/static/";

	Cloudinary cloudinary = new Cloudinary(
			ObjectUtils.asMap("cloud_name", "sukantamajhi", "api_key", "621956771347527",
					"api_secret", "MvdbHk02SRAU37eyltvjCuGhYug", "secure", true));

	public String upload(MultipartFile file) throws Exception {
		String filePath = "";
		try {
			String filename = file.getOriginalFilename();
			String filenameWithoutExtension;
			if (filename != null) {
				filenameWithoutExtension = filename.substring(0, filename.lastIndexOf("."));
			} else {
				filenameWithoutExtension = UUID.randomUUID().toString();
			}

			Path targetPath = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
			Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
			filePath = file.getOriginalFilename();

			var response = cloudinary.uploader().upload(UPLOAD_DIR + file.getOriginalFilename(),
					ObjectUtils.asMap("public_id", filenameWithoutExtension));

			java.io.File fileToDelete = new java.io.File(UPLOAD_DIR + file.getOriginalFilename());

			fileToDelete.delete();

			return (String) response.get("secure_url");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return filePath;
	}
}

