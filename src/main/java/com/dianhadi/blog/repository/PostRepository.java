package com.dianhadi.blog.repository;

import java.util.List;

import com.dianhadi.blog.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, String> {
    List<Post> findByAuthor(String author);
}
