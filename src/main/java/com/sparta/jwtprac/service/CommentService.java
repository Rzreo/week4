package com.sparta.jwtprac.service;


import com.sparta.jwtprac.controller.dto.request.CommentRequestDto;
import com.sparta.jwtprac.entity.Comment;
import com.sparta.jwtprac.entity.Member;
import com.sparta.jwtprac.entity.Post;
import com.sparta.jwtprac.repository.CommentRepository;
import com.sparta.jwtprac.repository.MemberRepository;
import com.sparta.jwtprac.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public CommentService(PostRepository postRepository,CommentRepository commentRepository,MemberRepository memberRepository ){
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.memberRepository = memberRepository;
    }
    public Comment upload(CommentRequestDto requestDto, UserDetails user) {
        Member man = memberRepository.findById(Long.parseLong(user.getUsername())).orElseThrow(
                () -> new RuntimeException("작성자 정보가 없습니다."));
        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(
                () -> new RuntimeException("해당 게시물이 없습니다."));
        Comment comment = new Comment(requestDto.getContent(),man.getNickname(),post);
        commentRepository.save(comment);
        return comment;
    }

    public List<Comment> show(Long post_id) {
        Post post = postRepository.findById(post_id).orElseThrow(
                ()->new RuntimeException("해당 게시물이 없습니다."));
        List<Comment> comments = commentRepository.findAllByOrderByModifiedAtDesc();
        List<Comment> output = new ArrayList<>();
        for(Comment c : comments){
            if(c.getPost().getId().equals(post.getId())){
                output.add(c);
            }
        }

        return output;
    }

    public Comment updateComment(Long id, CommentRequestDto requestDto, UserDetails user) {
        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(
                ()->new RuntimeException("해당 게시물이 없습니다."));
        Comment comment = commentRepository.findById(id).orElseThrow(
                ()->new RuntimeException("해당 댓글이 없습니다."));
        Member man = memberRepository.findById(Long.parseLong(user.getUsername())).orElseThrow(
                () -> new RuntimeException("작성자 정보가 없습니다."));
        if(!comment.getAuther().equals(man.getNickname())){
            throw new RuntimeException("작성자만 수정할 수 있습니다.");
        }
        comment.update(requestDto.getContent());
        return comment;
    }


    public boolean delete(Long id, UserDetails user) {
        Comment comment = commentRepository.findById(id).orElseThrow(
                ()->new RuntimeException("해당 댓글이 없습니다."));
        Member man = memberRepository.findById(Long.parseLong(user.getUsername())).orElseThrow(
                () -> new RuntimeException("작성자 정보가 없습니다."));
        if(!comment.getAuther().equals(man.getNickname())){
            throw new RuntimeException("작성자만 삭제할 수 있습니다.");
        }
        commentRepository.deleteById(id);
        return true;
    }
}
