package com.sparta.jwtprac.controller;

import com.sparta.jwtprac.controller.dto.request.MemberRequestDto;
import com.sparta.jwtprac.controller.dto.request.SignupRequestDto;
import com.sparta.jwtprac.controller.dto.response.ResponseDto;
import com.sparta.jwtprac.controller.dto.response.SignLogResponseDto;
import com.sparta.jwtprac.entity.Member;
import com.sparta.jwtprac.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseDto<SignLogResponseDto> signup(@RequestBody SignupRequestDto memberRequestDto) {
        Member member = authService.signup(memberRequestDto);
        return new ResponseDto<>(true,new SignLogResponseDto(member),null);
    }

    @PostMapping("/login")
    public ResponseDto<SignLogResponseDto> login(@RequestBody MemberRequestDto memberRequestDto, HttpServletResponse response) {
        return new ResponseDto<>(true,new SignLogResponseDto(authService.login(memberRequestDto, response)),null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseDto<Object> handleE(Exception e){
        return new ResponseDto<>(false,null,e.getMessage());
    }

}