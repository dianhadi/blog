package com.dianhadi.blog.repository;

import java.util.List;

import com.dianhadi.blog.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.cache.annotation.Cacheable;


public interface PostRepository extends JpaRepository<Post, String> {
    List<Post> findByAuthor(String author);
    
    @Cacheable(value = "posts", key = "#seoTitle", cacheManager = "redisCacheManagerWithTTL60")
    Post findBySeoTitle(String seoTitle);
}
