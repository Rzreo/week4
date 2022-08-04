package com.sparta.jwtprac.service;

import com.sparta.jwtprac.controller.dto.*;
import com.sparta.jwtprac.controller.dto.request.MemberRequestDto;
import com.sparta.jwtprac.controller.dto.request.SignupRequestDto;
import com.sparta.jwtprac.entity.Member;
import com.sparta.jwtprac.entity.RefreshToken;
import com.sparta.jwtprac.jwt.TokenProvider;
import com.sparta.jwtprac.repository.MemberRepository;
import com.sparta.jwtprac.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public Member signup(SignupRequestDto memberRequestDto) {

        if (memberRepository.existsByNickname(memberRequestDto.getNickname())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }

        Member member = memberRequestDto.toMember(passwordEncoder);
        memberRepository.save(member);
        return member;
    }

    @Transactional
    public Member login(MemberRequestDto memberRequestDto, HttpServletResponse response) {
        // 1. Login nickname과 pw를 사용하야 authenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto.toAuthentication();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //id/pw를 기준으로 authentication을 만듬
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);
        // 5. 토큰 발급
        response.setHeader("Authorization","Bearer " + tokenDto.getAccessToken());
        response.setHeader("Refresh-Token",tokenDto.getRefreshToken());

        return memberRepository.findByNickname(memberRequestDto.getNickname())
                .orElseThrow(() -> new UsernameNotFoundException(memberRequestDto.getNickname() + " -> 데이터베이스에서 찾을 수 없습니다."));
    }

    @Transactional
    public boolean reissue(TokenRequestDto tokenRequestDto, HttpServletResponse response) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            return false;
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = (Authentication) tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        response.setHeader("Access-Token",tokenDto.getAccessToken());
        response.setHeader("Access-Token",tokenDto.getRefreshToken());

        return true;
    }
}
