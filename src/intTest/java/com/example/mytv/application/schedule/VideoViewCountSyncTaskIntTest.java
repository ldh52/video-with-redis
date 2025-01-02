package com.example.mytv.application.schedule;

import com.example.mytv.application.port.out.SaveVideoPort;
import com.example.mytv.common.RedisKeyGenerator;
import com.example.mytv.config.TestRedisConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Random;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = TestRedisConfig.class)
public class VideoViewCountSyncTaskIntTest {
    @Autowired
    private VideoViewCountSyncTask sut;

    @Autowired
    private RedisTemplate<String, Long> longRedisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @SpyBean
    private SaveVideoPort saveVideoPort;

    @BeforeEach
    void setUp() {
        for (int i = 0; i < 10; i++) {
            var videoId = "videoId" + i;

            var viewCountKey = RedisKeyGenerator.getVideoViewCountKey(videoId);
            var viewCountValue = new Random().nextLong(1000);
            longRedisTemplate.opsForValue().set(viewCountKey, viewCountValue);

            var viewCountSetKey = RedisKeyGenerator.getVideoViewCountSetKey();
            stringRedisTemplate.opsForSet().add(viewCountSetKey, videoId);
        }
    }

    @Test
    void syncVideoViewCount() {
        sut.syncVideoViewCount();

        verify(saveVideoPort, times(10)).syncViewCount(any());
    }
}
