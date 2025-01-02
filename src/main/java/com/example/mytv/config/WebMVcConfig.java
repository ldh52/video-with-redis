package com.example.mytv.config;

import com.example.mytv.adapter.in.resolver.UserHandlerMethodArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMVcConfig implements WebMvcConfigurer {
    private final UserHandlerMethodArgumentResolver userHandlerMethodArgumentResolver;

    public WebMVcConfig(UserHandlerMethodArgumentResolver userHandlerMethodArgumentResolver) {
        this.userHandlerMethodArgumentResolver = userHandlerMethodArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
        resolvers.add(userHandlerMethodArgumentResolver);
    }
}
