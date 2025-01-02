package com.example.mytv.adapter.out;

import com.example.mytv.application.port.out.UserSessionPort;
import com.example.mytv.common.RedisKeyGenerator;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserSessionPersistenceAdapter implements UserSessionPort {
    private final StringRedisTemplate stringRedisTemplate;

    public UserSessionPersistenceAdapter(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public String getUserId(String authKey) {
        var valOperations = stringRedisTemplate.opsForValue();
        return stringRedisTemplate.opsForValue().get(RedisKeyGenerator.getUserSessionKey(authKey));
    }
}
