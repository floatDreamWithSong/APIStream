package com.daydreamer.faastest.configuration;

import com.daydreamer.faastest.interceptor.SDKInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SDKInterceptor()).addPathPatterns("/APIStreamModuleServiceSDK");
    }
}
