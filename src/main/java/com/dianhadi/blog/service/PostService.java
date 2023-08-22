package com.dianhadi.blog.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
}
