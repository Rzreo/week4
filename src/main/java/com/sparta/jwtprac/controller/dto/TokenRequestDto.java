package com.sparta.jwtprac.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenRequestDto {
    private String accessToken;
    private String refreshToken;

    public TokenRequestDto(String access, String refresh){
        this.accessToken = access;
        this.refreshToken = refresh;
    }
}