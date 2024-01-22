package com.sukanta.springbootecom.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sukanta.springbootecom.config.ApiResponse;
import com.sukanta.springbootecom.config.Constant;
import com.sukanta.springbootecom.config.JwtAuthService;
import com.sukanta.springbootecom.model.File;
import com.sukanta.springbootecom.model.user.User;
import com.sukanta.springbootecom.service.storageService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/storage")
@CrossOrigin(origins = "http://localhost:3000, http://localhost:3001, http://localhost:5500")
@Slf4j
public class storageController {

	private final JwtAuthService jwtAuthService;
	private final storageService storageService;

	public storageController(JwtAuthService jwtAuthService, storageService storageService) {
		this.jwtAuthService = jwtAuthService;
		this.storageService = storageService;
	}

	@PostMapping("")
	public ResponseEntity<ApiResponse<List<File>>> uploadFile(
			@RequestHeader(name = "Authorization") String token,
			@RequestParam("file") List<MultipartFile> files) {
		ApiResponse<List<File>> apiResponse = new ApiResponse<>();

		try {
			boolean tokenExpired = jwtAuthService.verifyJWT(token);
			if (tokenExpired) {
				apiResponse.setError(true);
				apiResponse.setCode("UNAUTHORIZED");
				apiResponse.setMessage(Constant.UNAUTHORIZED);

				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
			} else {
				log.info("Uploading file");
				User userDetails = jwtAuthService.getUser(token);
				if (userDetails != null) {

					if (files.size() == 0) {
						apiResponse.setError(true);
						apiResponse.setCode("NO_FILES_SELECTED");
						apiResponse.setMessage(Constant.NO_FILES_SELECTED);

						return ResponseEntity.badRequest().body(apiResponse);
					} else if (files.size() > 3) {
						apiResponse.setError(true);
						apiResponse.setCode("FILE_UPLOAD_FAILED");
						apiResponse.setMessage(Constant.FILE_UPLOAD_FAILED);
						apiResponse.setErr(new Exception("File size exceeded"));
						apiResponse.setErrMessage(Constant.MAX3FILESELECT);

						return ResponseEntity.badRequest().body(apiResponse);
					}

					List<File> fileObj = storageService.store(files);

					apiResponse.setError(false);
					apiResponse.setCode("FILE_UPLOADED");
					apiResponse.setMessage(Constant.FILE_UPLOADED);
					apiResponse.setData(fileObj);

					return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
				} else {
					apiResponse.setError(true);
					apiResponse.setCode("ACCESS_DENIED");
					apiResponse.setMessage(Constant.ACCESS_DENIED);
					return ResponseEntity.badRequest().body(apiResponse);
				}
			}
		} catch (Exception e) {
			log.error("Error in file upload", e);
			apiResponse.setError(true);
			apiResponse.setCode("FILE_UPLOAD_FAILED");
			apiResponse.setMessage(Constant.FILE_UPLOAD_FAILED);
			apiResponse.setErr(e);
			apiResponse.setErrMessage(e.getMessage());

			return ResponseEntity.internalServerError().body(apiResponse);
		}
	}

	@GetMapping("{fileName}")
	public ResponseEntity<ApiResponse<String>> getFile(@PathVariable String fileName,
			@RequestParam(required = false) Integer width,
			@RequestParam(required = false) Integer height,
			@RequestParam(required = false) String crop) {
		ApiResponse<String> apiResponse = new ApiResponse<>();
		try {
			String image = storageService.getFile(fileName, width, height, crop);

			apiResponse.setError(false);
			apiResponse.setCode("FILE_DOWNLOADED");
			apiResponse.setData(image);
			return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
		} catch (Exception e) {
			log.error("Error in file download", e);
			apiResponse.setError(true);
			apiResponse.setCode("FILE_DOWNLOAD_FAILED");
			// apiResponse.setMessage(Constant.FILE_DOWNLOAD_FAILED);
			apiResponse.setErr(e);
			return ResponseEntity.internalServerError().body(apiResponse);
		}
	}
}
