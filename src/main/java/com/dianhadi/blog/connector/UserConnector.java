package com.dianhadi.blog.connector;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

// import com.fasterxml.jackson.core.type.TypeReference;
// import com.fasterxml.jackson.databind.ObjectMapper;

import com.dianhadi.blog.model.User;
import com.dianhadi.blog.response.ApiResponse;
import com.dianhadi.blog.response.IdResponse;
import com.dianhadi.blog.utils.TokenGenerator;

@Component
public class UserConnector {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String userServiceBaseUrl = "http://app.golang.host:8008"; // Replace with the actual URL

    public User getUserByUsername(String username) {
        String url = userServiceBaseUrl + "/v1/user/" + username;
        String token = TokenGenerator.generateToken();

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Request-ID", UUID.randomUUID().toString());
        headers.add("Authorization", "Bearer " + token);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        

        ParameterizedTypeReference<ApiResponse<User>> responseType = new ParameterizedTypeReference<ApiResponse<User>>() {};
        ResponseEntity<ApiResponse<User>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, responseType);

        ApiResponse<User> apiResponse = responseEntity.getBody();

        User user = apiResponse.getData();

        return user;
    }

    public String getUserID(String userToken) {
        String url = userServiceBaseUrl + "/v1/authenticate";
        String serviceToken = TokenGenerator.generateToken();

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Request-ID", UUID.randomUUID().toString());
        headers.add("Authorization", "Bearer " + serviceToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("token", userToken);

        // Create the HTTP entity with headers and form data
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData, headers);

        // Send the POST request and handle the response
        ParameterizedTypeReference<ApiResponse<IdResponse>> responseType = new ParameterizedTypeReference<ApiResponse<IdResponse>>() {};
        ResponseEntity<ApiResponse<IdResponse>> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, responseType);

        ApiResponse<IdResponse> apiResponse = responseEntity.getBody();
        // Extract the id from the response
        String id = apiResponse.getData().getId();

        // Handle the extracted id
        return id;
    }
}
