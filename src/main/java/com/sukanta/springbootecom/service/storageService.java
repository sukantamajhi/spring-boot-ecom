package com.sukanta.springbootecom.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sukanta.springbootecom.model.File;
import com.sukanta.springbootecom.repository.StorageRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class storageService {

	@Autowired
	private StorageRepository storageRepository;

	public String store(MultipartFile file) throws Exception {
		try {
			String fileName = file.getOriginalFilename();
			String contentType = file.getContentType();
			byte[] fileContent = file.getBytes();
			File savefile =
					File.builder().fileName(fileName).type(contentType).data(fileContent).build();

			if (savefile != null) {
				storageRepository.save(savefile);
				return "File saved successfully";
			} else {
				throw new Exception("File is null");
			}
		} catch (Exception e) {
			log.error("Error in storing file==>> " + e);
			throw new Exception(e);
		}
	}

}
