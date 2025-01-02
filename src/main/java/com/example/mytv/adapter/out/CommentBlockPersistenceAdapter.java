package com.example.mytv.adapter.out;

import com.example.mytv.application.port.out.CommentBlockPort;
import com.example.mytv.common.RedisKeyGenerator;
import com.example.mytv.domain.user.User;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class CommentBlockPersistenceAdapter implements CommentBlockPort {
    private final StringRedisTemplate stringRedisTemplate;

    public CommentBlockPersistenceAdapter(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void saveUserCommentBlock(String userId, String commentId) {
        // user:comment-block:userId [commentId1, commentId2]
        stringRedisTemplate.opsForSet().add(RedisKeyGenerator.getUserCommentBlock(userId), commentId);
    }

    @Override
    public Set<String> getUserCommentBlocks(String userId) {
        return stringRedisTemplate.opsForSet().members(RedisKeyGenerator.getUserCommentBlock(userId));
    }
}
