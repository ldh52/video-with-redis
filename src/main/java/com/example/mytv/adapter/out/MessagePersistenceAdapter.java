package com.example.mytv.adapter.out;

import com.example.mytv.application.port.out.MessagePort;
import com.example.mytv.common.MessageTopics;
import com.example.mytv.domain.message.NewVideoMessage;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessagePersistenceAdapter implements MessagePort {
    private final RedisTemplate<String, Object> redisTemplate;

    public MessagePersistenceAdapter(@Lazy RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void sendNewVideMessage(String channelId) {
        redisTemplate.convertAndSend(MessageTopics.NEW_VIDEO, new NewVideoMessage(channelId));
    }

}
