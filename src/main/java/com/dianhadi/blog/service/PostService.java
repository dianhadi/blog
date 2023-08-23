package com.dianhadi.blog.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheEvict;

import com.dianhadi.blog.connector.UserConnector;
import com.dianhadi.blog.model.Post;
import com.dianhadi.blog.model.User;
import com.dianhadi.blog.repository.PostRepository;
import com.dianhadi.blog.response.BlogResponse;
import com.dianhadi.blog.utils.ContentPreviewGenerator;
import com.dianhadi.blog.utils.SeoTitleGenerator;

@Service
public class PostService {
    @Autowired
    private final PostRepository postRepository;

    // @PersistenceContext
    // private EntityManager entityManager;
    
    public PostService(PostRepository postRepository /*, EntityManager entityManager*/) {
        this.postRepository = postRepository;
        // this.entityManager = entityManager;
    }

    @Transactional
    public Post createPost(Post postRequest, String authorId) {

        String id = UUID.randomUUID().toString();

        postRequest.setId(id);
        postRequest.setAuthor(authorId);
        postRequest.setSeoTitle(SeoTitleGenerator.generateSeoUrl(postRequest.getTitle()));
        postRequest.setPreviewContent(ContentPreviewGenerator.generatePreviewContent(postRequest.getContent()));

        postRequest.setPublishDate(LocalDateTime.now());

        postRepository.save(postRequest);

        return postRequest;

    }

    @Transactional
    public boolean editPost(Post postRequest, String authorId) {
        Optional<Post> optionalPost = postRepository.findById(postRequest.getId());

        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();

            // Check if the author of the existing post matches the provided author ID
            if (existingPost.getAuthor().equals(authorId)) {
                String existingSeoTitle = existingPost.getSeoTitle();
                // Update fields of the existing post with values from the editedPost
                existingPost.setTitle(postRequest.getTitle());
                existingPost.setSeoTitle(SeoTitleGenerator.generateSeoUrl(postRequest.getTitle()));
                existingPost.setPreviewContent(ContentPreviewGenerator.generatePreviewContent(postRequest.getContent()));
                existingPost.setContent(postRequest.getContent());
                // existingPost.setPublishDate(LocalDateTime.now());

                // Save the updated post
                postRepository.save(existingPost);
                
                invalidateCacheByTitle(existingSeoTitle);
                return true; // Edit successful
            } else {
                return false; // Author mismatch, edit not allowed
            }
        } else {
            return false; // Post not found
        }
    }

    @CacheEvict(value = "posts", key = "#seoTitle", cacheManager = "redisCacheManagerWithTTL60")
    public void invalidateCacheByTitle(String seoTitle) {
        // This method is annotated with @CacheEvict, so it will automatically invalidate the cache entry
        System.out.println("delete this: " + seoTitle);
    }

}
