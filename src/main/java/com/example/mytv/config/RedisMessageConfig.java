package com.example.mytv.config;

import com.example.mytv.application.listener.RedisNewVideoMessageListener;
import com.example.mytv.common.MessageTopics;
import com.example.mytv.domain.message.NewVideoMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisMessageConfig {
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RedisNewVideoMessageListener redisNewVideoMessageListener;

    @Bean
    RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        final RedisTemplate<String,Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(NewVideoMessage.class));
        return redisTemplate;
    }

    @Bean
    RedisMessageListenerContainer redisContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(newVideoListener(), new ChannelTopic(MessageTopics.NEW_VIDEO));
        return container;
    }

    @Bean
    MessageListenerAdapter newVideoListener() {
        return new MessageListenerAdapter(redisNewVideoMessageListener);
    }

}
