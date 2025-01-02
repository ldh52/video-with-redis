package com.example.mytv.adapter.out.redis.channel;

import static org.assertj.core.api.BDDAssertions.then;

import com.example.mytv.config.TestRedisConfig;
import com.example.mytv.domain.channel.ChannelFixtures;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestRedisConfig.class)
public class ChannelRedisRepositoryIntTest {
    @Autowired
    private ChannelRedisRepository channelRedisRepository;

    @Test
    void testIndexedProperty() {
        // given
        var channel = ChannelFixtures.stub("channelId");
        var redisHash = ChannelRedisHash.from(channel);
        channelRedisRepository.save(redisHash);

        channelRedisRepository.findById("channelId");
        var result = channelRedisRepository.findAllByContentOwnerId("user");
        then(result).hasSize(1);
    }
}
