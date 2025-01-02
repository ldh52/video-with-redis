package com.example.mytv.adapter.out;

import static com.example.mytv.common.RedisKeyGenerator.getCommentLikeKey;

import com.example.mytv.application.port.out.CommentLikePort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class CommentLikePersistenceAdapter implements CommentLikePort {
    private final RedisTemplate<String, Long> redisTemplate;

    public CommentLikePersistenceAdapter(RedisTemplate<String, Long> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Long getCommentLikeCount(String commentId) {
        var likeCount = redisTemplate.opsForValue().get(getCommentLikeKey(commentId));
        return likeCount == null ? 0 : likeCount;
    }
}
