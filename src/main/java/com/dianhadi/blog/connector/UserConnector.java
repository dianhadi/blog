package com.dianhadi.blog.connector;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
// import com.fasterxml.jackson.core.type.TypeReference;
// import com.fasterxml.jackson.databind.ObjectMapper;

import com.dianhadi.blog.model.User;
import com.dianhadi.blog.response.ApiResponse;
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
}
