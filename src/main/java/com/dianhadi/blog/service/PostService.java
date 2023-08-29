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
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
// import com.rabbitmq.client.Connection;
// import com.rabbitmq.client.ConnectionFactory;
// import com.rabbitmq.client.Channel;

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
    private final ObjectMapper objectMapper;
    private final RabbitTemplate rabbitTemplate;
    
    public PostService(PostRepository postRepository, ObjectMapper objectMapper, RabbitTemplate rabbitTemplate) {
        this.postRepository = postRepository;
        this.objectMapper = objectMapper;
        this.rabbitTemplate = rabbitTemplate;
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

        try {
            String message = objectMapper.writeValueAsString(postRequest);
            rabbitTemplate.convertAndSend("post-created", message);
            // rabbitTemplate.convertAndSend("post-created", message);
        } catch (JsonProcessingException e) {
            // Handle the exception, e.g., log an error message
            e.printStackTrace();
        }

        // ConnectionFactory factory = new ConnectionFactory();
        // factory.setHost("rabbitmq");
        
        // try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
        //     // Declare the queue
        //     channel.queueDeclare("post-created", false, false, false, null);
            
        //     // Publish a message directly to the queue using the default exchange
        //     String message = objectMapper.writeValueAsString(postRequest);
        //     channel.basicPublish("", "post-created", null, message.getBytes());
        // } catch (Exception e) {
        //     // Handle the exception, you can log it or throw a custom exception if needed
        // }
       

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
