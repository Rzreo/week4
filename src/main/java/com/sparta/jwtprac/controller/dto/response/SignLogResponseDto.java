package com.sparta.jwtprac.controller.dto.response;

import com.sparta.jwtprac.entity.Member;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SignLogResponseDto {
    private Long id;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public SignLogResponseDto(Member member){
        this.id = member.getId();
        this.nickname = member.getNickname();
        this.createdAt = member.getCreatedAt();
        this.modifiedAt = member.getModifiedAt();
    }
}
