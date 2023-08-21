package com.dianhadi.blog.response;

import java.util.List;

import com.dianhadi.blog.model.User;
import com.dianhadi.blog.model.Post;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlogResponse {

    private User user;
    private List<Post> posts;

    // Constructor, getters, setters for user and posts
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
