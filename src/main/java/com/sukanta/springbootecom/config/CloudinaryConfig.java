package com.sukanta.springbootecom.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

public class CloudinaryConfig {
	Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap("cloud_name", "sukantamajhi",
			"api_key", "621956771347527", "api_secret", "MvdbHk02SRAU37eyltvjCuGhYug"));
}
