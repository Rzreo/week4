package com.sparta.jwtprac.entity;

import com.sparta.jwtprac.controller.dto.request.PostRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter // get 함수를 일괄적으로 만들어줍니다.
@NoArgsConstructor // 기본 생성자를 만들어줍니다.
@Entity
public class Post extends Timestamped{
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(nullable = false)
    private String title;

    private String content;

    @OneToMany
    private List<Comment> comments;
    @ManyToOne
    @JoinColumn(name = "MEMBER", nullable = false)
    private Member member;




    public Post(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.comments = new ArrayList<>();
    }


    public Post(PostRequestDto requestDto, Member member){
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.member = member;
        this.comments = new ArrayList<>();
    }

    public void update(PostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }

    public void updateComment(Comment comment) {
        for(Comment c : comments){
            if(c.getId().equals(comment.getId())){
                c.update(comment.getContent());
            }
        }
    }
}
