package com.sparta.jwtprac.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter // get 함수를 일괄적으로 만들어줍니다.
@NoArgsConstructor // 기본 생성자를 만들어줍니다.
@Entity
public class Comment extends Timestamped{
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(nullable = false)
    private String content;


    @Column(nullable = false)
    private String auther;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "POST", nullable = false)
    private Post post;

    public Comment(String content, String a, Post post) {
        this.content = content;
        this.auther = a;
        this.post = post;
    }

    public void update(String content) {
        this.content=content;
    }
}