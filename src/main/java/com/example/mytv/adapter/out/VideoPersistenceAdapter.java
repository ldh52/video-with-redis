package com.example.mytv.adapter.out;

import com.example.mytv.adapter.out.jpa.video.VideoJpaEntity;
import com.example.mytv.adapter.out.jpa.video.VideoJpaRepository;
import com.example.mytv.application.port.out.LoadVideoPort;
import com.example.mytv.application.port.out.SaveVideoPort;
import com.example.mytv.common.RedisKeyGenerator;
import com.example.mytv.domain.video.Video;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static com.example.mytv.common.CacheNames.VIDEO;
import static com.example.mytv.common.CacheNames.VIDEO_LIST;
import static com.example.mytv.common.RedisKeyGenerator.getVideoViewCountKey;

@Component
public class VideoPersistenceAdapter implements LoadVideoPort, SaveVideoPort {
    private final VideoJpaRepository videoJpaRepository;
    private final RedisTemplate<String, Long> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    public VideoPersistenceAdapter(VideoJpaRepository videoJpaRepository, RedisTemplate<String, Long> redisTemplate, StringRedisTemplate stringRedisTemplate) {
        this.videoJpaRepository = videoJpaRepository;
        this.redisTemplate = redisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    @Cacheable(cacheNames = VIDEO, key = "#videoId")
    public Video loadVideo(String videoId) {
        return videoJpaRepository.findById(videoId)
            .map(VideoJpaEntity::toDomain)
            .orElseThrow();
    }

    @Override
    @Cacheable(cacheNames = VIDEO_LIST, key = "#channelId")
    public List<Video> loadVideoByChannel(String channelId) {
        return videoJpaRepository.findByChannelId(channelId).stream()
            .map(VideoJpaEntity::toDomain)
            .toList();
    }

    @Override
    @Caching(evict = {
        @CacheEvict(cacheNames = VIDEO_LIST, key = "#video.channelId"),
        @CacheEvict(cacheNames = VIDEO, key = "#video.id")
    })
    public void saveVideo(Video video) {
        videoJpaRepository.save(VideoJpaEntity.from(video));
    }

    @Override
    public void incrementViewCount(String videoId) {
        var videoViewCountKey = getVideoViewCountKey(videoId);
        redisTemplate.opsForValue().increment(videoViewCountKey);

//        Using RedisAtomicLong
//        RedisAtomicLong redisAtomicLong = new RedisAtomicLong(videoViewCountKey, redisTemplate.getConnectionFactory());
//        redisAtomicLong.incrementAndGet();

//        stringRedisTemplate.opsForSet().add(getVideoViewCountSetKey(), videoId);
    }

    @Override
    public Long getViewCount(String videoId) {
        var videoViewCountKey = getVideoViewCountKey(videoId);
        var viewCont = redisTemplate.opsForValue().get(videoViewCountKey);
        return viewCont == null ? 0 : viewCont;
    }

    @Override
    public List<String> getAllVideoIdsWithViewCount() {
        var members = stringRedisTemplate.opsForSet().members(RedisKeyGenerator.getVideoViewCountSetKey());
        if (members == null) {
            return Collections.emptyList();
        }

        return members.stream().toList();
    }

    @Override
    public void syncViewCount(String videoId) {
        videoJpaRepository.findById(videoId)
            .ifPresent(videoJpaEntity -> {
                // video:view-count:videoId
                var viewCount = redisTemplate.opsForValue().get(RedisKeyGenerator.getVideoViewCountKey(videoId));
                videoJpaEntity.updateViewCount(redisTemplate.opsForValue().get(RedisKeyGenerator.getVideoViewCountKey(videoId)));
                videoJpaRepository.save(videoJpaEntity);

                redisTemplate.opsForSet().remove(RedisKeyGenerator.getVideoViewCountSetKey(), videoId);
            });
    }
}