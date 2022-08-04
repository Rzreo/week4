package com.sparta.jwtprac.service;
import com.sparta.jwtprac.controller.dto.request.PostRequestDto;
import com.sparta.jwtprac.entity.Member;
import com.sparta.jwtprac.entity.Post;
import com.sparta.jwtprac.repository.MemberRepository;
import com.sparta.jwtprac.repository.PostRepository;
import com.sparta.jwtprac.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public Post upload(PostRequestDto requestDto){
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));
        Post post = new Post(requestDto,member);
        postRepository.save(post);
        return post;
    }

    public List<Post> showPosts() {
        return postRepository.findAllByOrderByModifiedAtDesc();
    }

    public Post showDetail(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new NullPointerException("아이디가 존재하지 않습니다.")
        );
    }

    public Post update(Long id, PostRequestDto requestDto, UserDetails user) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new NullPointerException("아이디가 존재하지 않습니다.")
        );

        System.out.println(user.getUsername());
        if(!(post.getMember().getId()==Long.parseLong(user.getUsername())))
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");

        post.update(requestDto);
        return post;
    }

    public boolean delete(Long id, UserDetails user) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new NullPointerException("아이디가 존재하지 않습니다.")
        );

        if(!(post.getMember().getId()==Long.parseLong(user.getUsername())))
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");

        postRepository.deleteById(id);
        return true;
    }
}
