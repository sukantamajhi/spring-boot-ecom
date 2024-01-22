package com.sukanta.springbootecom.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.sukanta.springbootecom.helper.Uploader;
import com.sukanta.springbootecom.model.File;
import com.sukanta.springbootecom.repository.StorageRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class storageService {
	Cloudinary cloudinary = new Cloudinary(
			ObjectUtils.asMap("cloud_name", "sukantamajhi", "api_key", "621956771347527",
					"api_secret", "MvdbHk02SRAU37eyltvjCuGhYug", "secure", true));

	@Autowired
	private StorageRepository storageRepository;

	@Autowired
	private Uploader uploader;

	public List<File> store(List<MultipartFile> files) throws Exception {
		try {
			List<File> fileList = new ArrayList<>();

			for (MultipartFile file : files) {
				File getOldFile = storageRepository.findByFileName(file.getOriginalFilename());

				if (getOldFile != null) {
					storageRepository.delete(getOldFile);
				}

				String secure_url = uploader.upload(file);

				File fileObj = File.builder().fileName(file.getOriginalFilename()).url(secure_url)
						.type(file.getContentType()).build();

				if (fileObj != null) {
					File newFile = storageRepository.save(fileObj);

					fileList.add(newFile);
				} else {
					throw new Exception("Failed to save file to database");
				}
			}
			return fileList;
		} catch (Exception e) {
			log.error("Error in storing file==>> " + e);
			throw new Exception(e);
		}
	}

	public String getFile(String fileName, Integer width, Integer height, String crop)
			throws Exception {
		try {
			var transformation = new Transformation<>();

			String url = cloudinary.url()
					.transformation(
							transformation.width(width).height(height).crop(crop).quality("90"))
					.generate(fileName);

			return url;
		} catch (Exception e) {
			log.error("Error in getting file==>> " + e);
			throw new Exception(e);
		}
	}
}

