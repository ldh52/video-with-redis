package com.example.mytv.adapter.out;

import com.example.mytv.adapter.out.redis.channel.ChannelRedisHash;
import com.example.mytv.adapter.out.redis.channel.ChannelRedisRepository;
import com.example.mytv.adapter.out.redis.user.UserRedisHash;
import com.example.mytv.adapter.out.redis.user.UserRedisRepository;
import com.example.mytv.config.TestRedisConfig;
import com.example.mytv.domain.channel.ChannelFixtures;
import com.example.mytv.domain.user.UserFixtures;
import com.example.mytv.common.CacheNames;
import com.example.mytv.common.RedisKeyGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.UUID;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest(classes = TestRedisConfig.class)
public class CacheManagerPersistenceAdapterIntTest {
    @Autowired
    private CacheManagePersistenceAdapter sut;

    @Autowired
    private ChannelRedisRepository channelRedisRepository;
    @Autowired
    private UserRedisRepository userRedisRepository;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate<String, Long> longRedisTemplate;

    @BeforeEach
    void setUp() {
        channelRedisRepository.save(ChannelRedisHash.from(ChannelFixtures.stub("channelId")));
        userRedisRepository.save(UserRedisHash.from(UserFixtures.stub("userId")));
        stringRedisTemplate.opsForSet().add(RedisKeyGenerator.getVideoLikeKey("videoId"), "userId");
        longRedisTemplate.opsForValue().get(RedisKeyGenerator.getVideoViewCountKey("videoId"));
        stringRedisTemplate.opsForValue().set(RedisKeyGenerator.getUserSessionKey(UUID.randomUUID().toString()), "userId");
        longRedisTemplate.opsForValue().set(RedisKeyGenerator.getCommentLikeKey("commentId"), 20L);
        stringRedisTemplate.opsForValue().set(RedisKeyGenerator.getPinnedCommentKey("videoId"), "commentId");
    }

    @Test
    public void testCacheKeys() {
        sut.getAllKeys()
            .forEach(key -> System.out.println(key));
    }

    @Test
    public void testCacheKeysByPattern() {
        sut.getAllCacheNames("channel")
            .forEach(key -> System.out.println(key));
    }

    @Test
    public void testCacheNames() {
        CacheNames.getCacheNames()
            .forEach(key -> System.out.println(key));
    }

    @Test
    public void testDeleteCache() {
        sut.deleteCache("comment:like:commentId");

        then(longRedisTemplate.opsForValue().get(RedisKeyGenerator.getCommentLikeKey("commentId")))
            .isNull();
    }
}
