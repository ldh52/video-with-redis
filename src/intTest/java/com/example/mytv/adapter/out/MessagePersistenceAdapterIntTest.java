package com.example.mytv.adapter.out;

import com.example.mytv.adapter.out.jpa.channel.ChannelJpaEntity;
import com.example.mytv.adapter.out.jpa.channel.ChannelJpaRepository;
import com.example.mytv.adapter.out.jpa.subscribe.SubscribeJpaEntity;
import com.example.mytv.adapter.out.jpa.subscribe.SubscribeJpaRepository;
import com.example.mytv.adapter.out.jpa.user.UserJpaEntity;
import com.example.mytv.adapter.out.jpa.user.UserJpaRepository;
import com.example.mytv.config.TestRedisConfig;
import com.example.mytv.domain.channel.ChannelFixtures;
import com.example.mytv.domain.user.UserFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest(classes = TestRedisConfig.class)
public class MessagePersistenceAdapterIntTest {
    @Autowired
    private MessagePersistenceAdapter sut;
    @Autowired
    private ChannelJpaRepository channelJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private SubscribeJpaRepository subscribeJpaRepository;

    @BeforeEach
    public void setUp() {
        // 여러 개의 구독 정보 생성
        var channelJpaEntity = channelJpaRepository.save(ChannelJpaEntity.from(ChannelFixtures.stub("channelId")));
        for (int i = 0; i < 5; i++) {
            var userJpaEntity = userJpaRepository.save(UserJpaEntity.from(UserFixtures.stub(UUID.randomUUID().toString())));
            var subscribeJpaEntity = new SubscribeJpaEntity(
                UUID.randomUUID().toString(),
                channelJpaEntity,
                userJpaEntity
            );
            subscribeJpaRepository.save(subscribeJpaEntity);
        }
    }

    @Test
    public void testMessagePubSub() {
        sut.sendNewVideMessage("channelId");
    }
}
