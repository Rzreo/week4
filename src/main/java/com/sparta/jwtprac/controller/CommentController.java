package com.sparta.jwtprac.controller;


import com.sparta.jwtprac.controller.dto.request.CommentRequestDto;
import com.sparta.jwtprac.controller.dto.response.ResponseDto;
import com.sparta.jwtprac.entity.Comment;
import com.sparta.jwtprac.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @PostMapping("/auth/comment")
    public ResponseDto<Comment> upload(@RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetails user){
        return new ResponseDto<>(true,commentService.upload(requestDto,user),null);
    }

    @GetMapping("/comment/{id}")
    public ResponseDto<List<Comment>> showAll(@PathVariable Long id){
        return new ResponseDto<>(true,commentService.show(id),null);
    }

    @PutMapping("/auth/comment/{id}")
    public ResponseDto<Comment> update(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetails user){
        return new ResponseDto<>(true,commentService.updateComment(id,requestDto,user),null);
    }

    @DeleteMapping("/auth/comment/{id}")
    public ResponseDto<Boolean> deleteComment(@PathVariable Long id, @AuthenticationPrincipal UserDetails user){
        return new ResponseDto<>(true,commentService.delete(id,user),null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseDto<Object> handleE(Exception e){
        return new ResponseDto<>(false,null,e.getMessage());
    }
}
