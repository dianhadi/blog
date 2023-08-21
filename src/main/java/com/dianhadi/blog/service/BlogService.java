package com.dianhadi.blog.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dianhadi.blog.connector.UserConnector;
import com.dianhadi.blog.model.Post;
import com.dianhadi.blog.model.User;
import com.dianhadi.blog.repository.PostRepository;
import com.dianhadi.blog.response.ApiResponse;
import com.dianhadi.blog.response.BlogResponse; 

@Service
public class BlogService {
    private final PostRepository postRepository;
    private final UserConnector userConnector;

    public BlogService(PostRepository postRepository, UserConnector userConnector) {
        this.postRepository = postRepository;
        this.userConnector = userConnector;
    }

    public BlogResponse getUserBlog(String username) {
        User user = userConnector.getUserByUsername(username);
        List<Post> posts = postRepository.findByAuthor(user.getID());
        System.out.println(posts.toString());

        BlogResponse blogResponse = new BlogResponse();
        blogResponse.setUser(user);
        blogResponse.setPosts(posts);

        return blogResponse;

    }
}