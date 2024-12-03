package com.example.demo.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomConfig {

    @Value("${custom.property}")
    private String customProperty;

    public String getCustomProperty() {
        return customProperty;
    }
}