package com.sparta.jwtprac.controller;

import com.sparta.jwtprac.controller.dto.request.PostRequestDto;
import com.sparta.jwtprac.controller.dto.response.ResponseDto;
import com.sparta.jwtprac.entity.Post;
import com.sparta.jwtprac.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/auth/post")
    public ResponseDto<Post> upload(@RequestBody PostRequestDto requestDto){
        Post post = postService.upload(requestDto);
        return new ResponseDto<>(true,post,null);
    }

    @GetMapping("/post")
    public ResponseDto<List<Post>> showAll(){
        return new ResponseDto<>(true,postService.showPosts(),null);
    }

    @GetMapping("/post/{id}")
    public ResponseDto<Post> showPost(@PathVariable Long id){
        return new ResponseDto<>(true,postService.showDetail(id),null);
    }

    @PutMapping("/auth/post/{id}")
    public ResponseDto<Post> updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetails user){
        return new ResponseDto<>(true,postService.update(id,requestDto, user),null);
    }

    @DeleteMapping("/auth/post/{id}")
    public ResponseDto<Boolean> deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetails user){
        return new ResponseDto<>(true,postService.delete(id, user),null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseDto<Object> handleE(Exception e){
        return new ResponseDto<>(false,null,e.getMessage());
    }
}
