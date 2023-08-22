package com.dianhadi.blog.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
// import javax.servlet.annotation.WebFilter;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.dianhadi.blog.connector.UserConnector;
import com.dianhadi.blog.model.User;
import com.dianhadi.blog.repository.PostRepository;

import java.io.IOException;

// @WebFilter("/v1/post/*")
@Component
public class TokenFilter implements Filter {

    private final UserConnector userConnector;

    @Autowired // Optional if you're using Spring 4.3+
    public TokenFilter(UserConnector userConnector) {
        this.userConnector = userConnector;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code, if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        if (requestURI.startsWith("/v1/post/")) {
            // Retrieve the Authorization header
            String authorizationHeader = httpRequest.getHeader("Authorization");
            String token;

            String[] parts = authorizationHeader.split(" "); // Split by space
            if (parts.length == 2) {
                token = parts[1];
            } else {
                // If id is empty, block the request
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN); // Set the appropriate status code
                return;
            }

            // Perform token validation logic here
            String userID = userConnector.getUserID(token);

            // Check if the id is empty
            if (userID == null || userID.isEmpty()) {
                // If id is empty, block the request
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN); // Set the appropriate status code
                return;
            }

            // Add the id to the request's attributes
            request.setAttribute("userId", userID);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup code, if needed
    }
}
