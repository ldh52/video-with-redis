package com.example.mytv.adapter.out;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.mytv.adapter.out.jpa.video.VideoJpaRepository;
import com.example.mytv.config.TestRedisConfig;
import com.example.mytv.domain.video.Video;
import com.example.mytv.common.RedisKeyGenerator;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = TestRedisConfig.class)
@Transactional
@Sql("/com/example/mytv/adapter/out/VideoPersistenceAdapterIntTest.sql")
public class VideoPersistenceAdapterIntTest {
    @Autowired
    private VideoPersistenceAdapter sut;

    @SpyBean
    private VideoJpaRepository videoJpaRepository;
    @Autowired
    private RedisTemplate<String, Long> redisTemplate;
    @Autowired
    private RedisCacheManager redisCacheManager;

    @Test
    void loadVideo() {
        var videoId = "video1";
        for (int i = 0; i < 3; i++) {
            sut.loadVideo(videoId);
        }

        verify(videoJpaRepository, times(1)).findById(videoId);
    }

    @Test
    void loadVideoByChannel() {
        for (int i = 0; i < 3; i++) {
            sut.loadVideoByChannel("channelId");
        }

        verify(videoJpaRepository, times(1)).findByChannelId("channelId");
    }

    @Test
    void saveVideoThenEvictVideoListCache() {
        // Given
        sut.loadVideoByChannel("channel1");
        System.out.println(redisCacheManager.getCache("video:list").get("channel1"));

        var video = Video.builder()
            .id("video3").title("video3").description("video3").thumbnailUrl("https://example.com/image.jpg")
            .channelId("channel1").publishedAt(LocalDateTime.now())
            .build();

        // When
        sut.saveVideo(video);

        // Then
        then(redisCacheManager.getCache("video:list").get("channel1"))
            .isNull();
    }

    @Test
    void saveVideoThenEvictVideoCache() {
        // Given
        var video = sut.loadVideo("video1");
        System.out.println(redisCacheManager.getCache("video").get("video1"));

        var updatedVideo = Video.builder()
            .id(video.getId()).title(video.getTitle()).description(video.getDescription()).thumbnailUrl(video.getThumbnailUrl())
            .viewCount(200L)
            .channelId(video.getChannelId()).publishedAt(video.getPublishedAt())
            .build();

        // When
        sut.saveVideo(updatedVideo);

        // Then
        then(redisCacheManager.getCache("video").get("video1"))
            .isNull();
    }

    @Test
    void incrVideoViewCount() {
        for (int i = 0; i < 5; i++) {
            sut.incrementViewCount("video1");
        }

        var result = redisTemplate.opsForValue().get(RedisKeyGenerator.getVideoViewCountKey("video1"));
        then(result).isEqualTo(5L);
    }
}
