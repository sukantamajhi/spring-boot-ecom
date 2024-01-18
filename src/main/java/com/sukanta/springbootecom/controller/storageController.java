package com.sukanta.springbootecom.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sukanta.springbootecom.config.ApiResponse;
import com.sukanta.springbootecom.config.Constant;
import com.sukanta.springbootecom.config.JwtAuthService;
import com.sukanta.springbootecom.model.enums.Role;
import com.sukanta.springbootecom.model.user.User;
import com.sukanta.springbootecom.service.storageService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/storage")
@CrossOrigin(origins = "http://localhost:3000, http://localhost:3001")
@Slf4j
public class storageController {

	private final JwtAuthService jwtAuthService;
	private final storageService storageService;

	public storageController(JwtAuthService jwtAuthService, storageService storageService) {
		this.jwtAuthService = jwtAuthService;
		this.storageService = storageService;
	}

	@PostMapping("")
	public ResponseEntity<ApiResponse<String>> uploadFile(
			@RequestHeader(name = "Authorization") String token, @RequestBody MultipartFile file) {
		ApiResponse<String> apiResponse = new ApiResponse<>();

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
				if (userDetails != null && userDetails.getRole() == Role.ADMIN) {
					String fileName = storageService.store(file);

					log.info(fileName + " <<== new file name");

					apiResponse.setError(false);
					apiResponse.setCode("FILE_UPLOADED");
					apiResponse.setMessage(Constant.FILE_UPLOADED);

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

			return ResponseEntity.internalServerError().body(apiResponse);
		}
	}

	@GetMapping("")
	public ResponseEntity<ApiResponse<String>> getFile() {
		ApiResponse<String> apiResponse = new ApiResponse<>();
		try {
			apiResponse.setError(false);
			apiResponse.setCode("FILE_DOWNLOADED");
			// apiResponse.setMessage(Constant.FILE_DOWNLOADED);
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
