package com.dianhadi.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dianhadi.blog.response.ApiResponse;
import com.dianhadi.blog.response.BlogResponse;
import com.dianhadi.blog.service.BlogService;

@RestController
@RequestMapping("/v1/blog")
public class BlogController {
    private final BlogService blogService;

    @Autowired
    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/{username}")
    public ApiResponse<BlogResponse> getUserBlog(@PathVariable String username) {
        BlogResponse blogResponse = blogService.getUserBlog(username);
        
        ApiResponse<BlogResponse> apiResponse = new ApiResponse<>(200, "Success", blogResponse);

        return apiResponse;
    }
}