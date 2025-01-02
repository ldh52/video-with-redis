package com.example.mytv.application.listener;

import com.example.mytv.adapter.out.jpa.subscribe.SubscribeJpaEntity;
import com.example.mytv.adapter.out.jpa.subscribe.SubscribeJpaRepository;
import com.example.mytv.application.port.out.MessagePort;
import com.example.mytv.domain.message.NewVideoMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class RedisNewVideoMessageListener implements MessageListener {
    private final SubscribeJpaRepository subscribeJpaRepository;
    private final MessagePort messagePort;
    private final ObjectMapper objectMapper;

    public RedisNewVideoMessageListener(SubscribeJpaRepository subscribeJpaRepository, MessagePort messagePort, ObjectMapper objectMapper) {
        this.subscribeJpaRepository = subscribeJpaRepository;
        this.messagePort = messagePort;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            NewVideoMessage newVideoMessage = objectMapper.readValue(message.getBody(), NewVideoMessage.class);
            var channelId = newVideoMessage.getChannelId();

            subscribeJpaRepository.findAllByChannelId(channelId).stream()
                .map(SubscribeJpaEntity::getUser)
                .forEach(user -> System.out.println( user.getId() + "," + channelId + " 채널에 새로운 영상이 등록되었습니다."));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
