package com.dianhadi.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

import com.dianhadi.blog.response.ApiResponse;
import com.dianhadi.blog.response.BlogResponse;
import com.dianhadi.blog.response.IdResponse;
import com.dianhadi.blog.model.Post;
import com.dianhadi.blog.service.PostService;

@RestController
@RequestMapping("/v1/post")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/create")
    public ApiResponse<Post> createPost(@RequestBody Post postRequest, HttpServletRequest request) {
        String userID = (String) request.getAttribute("userId");
        
        Post postResponse = postService.createPost(postRequest, userID);
        
        ApiResponse<Post> apiResponse = new ApiResponse<>(200, "Success", postResponse);

        return apiResponse;
    }
}
