package com.sparta.jwtprac.controller.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDto<T> {
    private boolean success;

    private T data;

    private String error;

    public ResponseDto(boolean success, T data, String error){
        this.success = success;
        this.data = data;
        this.error = error;
    }
}