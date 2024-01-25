package com.sukanta.springbootecom.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
	@JsonProperty(required = true)
	private boolean error;
	@JsonProperty(required = true)
	private String code;
	@JsonProperty(required = true)
	private String message;
	private String access_token;
	private T data;
	private Exception err;
	private String errorMessage;
}
