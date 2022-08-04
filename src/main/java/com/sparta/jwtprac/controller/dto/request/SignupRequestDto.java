package com.sparta.jwtprac.controller.dto.request;

import com.sparta.jwtprac.entity.Authority;
import com.sparta.jwtprac.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequestDto {
    private String nickname;
    private String password;

    private String passwordConfirm;


    public Member toMember(PasswordEncoder passwordEncoder) {
        if(!password.equals(passwordConfirm)) throw new IllegalArgumentException("비밀번호와 비밀번호확인이 다릅니다.");
        if(nickname.length()<4 || nickname.length()>12) throw new IllegalArgumentException("닉네임 글자수는 4자 이상 12자 이하입니다.");
        if(password.length()<4 || password.length()>32) throw new IllegalArgumentException("비밀번호 글자수는 4자 이상 12자 이하입니다.");
        Pattern namePattern = Pattern.compile("^[0-9a-zA-Z]*$");
        Matcher nameMatcher = namePattern.matcher(nickname);
        if(!nameMatcher.matches()) throw new IllegalArgumentException("닉네임은 소문자,대문자,숫자만 사용할 수 있습니다.");
        Pattern passwordPattern = Pattern.compile("^[0-9a-z]*$");
        Matcher passwordMatcher = passwordPattern.matcher(password);
        if(!passwordMatcher.matches()) throw new IllegalArgumentException("비밀번호는 소문자,숫자만 사용할 수 있습니다.");

        return Member.builder()
                .nickname(nickname)
                .password(passwordEncoder.encode(password))
                .authority(Authority.ROLE_USER)
                .build();
    }

}
