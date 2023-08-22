package com.dianhadi.blog.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
// import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;

import com.dianhadi.blog.connector.UserConnector;

public class FilterConfig {
    private final UserConnector userConnector;

    @Autowired // Optional if you're using Spring 4.3+
    public FilterConfig(UserConnector userConnector) {
        this.userConnector = userConnector;
    }

    // @Bean
    public FilterRegistrationBean<TokenFilter> tokenFilter(UserConnector userConnector) {
        FilterRegistrationBean<TokenFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TokenFilter(userConnector));

        registrationBean.addUrlPatterns("/v1/post/*"); // Specify the path to intercept
        return registrationBean;
    }
}
