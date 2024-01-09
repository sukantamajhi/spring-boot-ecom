package com.sukanta.springbootecom.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)
@Getter
@Setter
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

    @Override
    public String toString() {
        return "ApiResponse{" +
                "error=" + error +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", access_token='" + access_token + '\'' +
                ", data=" + data +
                ", err=" + err +
                '}';
    }
}
