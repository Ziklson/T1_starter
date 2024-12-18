package com.kuvarin.starter.config;

import com.kuvarin.starter.aspect.ControllerAspect;
import com.kuvarin.starter.aspect.LogAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(LoggerProperties.class)
public class LoggerAutoConfiguration {

    private final LoggerProperties loggerProperties;

    public LoggerAutoConfiguration(LoggerProperties loggerProperties) {
        this.loggerProperties = loggerProperties;
    }


    @Bean
    public LogAspect logAspect() {
        return new LogAspect();
    }

    @Bean
    @ConditionalOnProperty(
            value = "logger.controller.enabled",
            havingValue = "true",
            matchIfMissing = true
    )
    public ControllerAspect controllerAspect() {
        return new ControllerAspect(loggerProperties.getLevel());
    }

}
