package com.example.mytv.adapter.out;

import com.example.mytv.application.port.out.CacheManagePort;
import com.example.mytv.common.CacheNames;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class CacheManagePersistenceAdapter implements CacheManagePort {
    private final StringRedisTemplate stringRedisTemplate;

    public CacheManagePersistenceAdapter(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public List<String> getAllCacheNames() {
        // getAllKeys() 참조
        // stringRedisTemplate.keys("*").stream().toList();
        return CacheNames.getCacheNames();
    }

    @Override
    public List<String> getAllCacheNames(String pattern) {
        // Redis KEYS operation
        var keys = stringRedisTemplate.keys(pattern + "*");

        if (keys == null) {
            return Collections.emptyList();
        }
        return keys.stream().toList();
    }

    @Override
    public Boolean deleteCache(String cacheKey) {
        return stringRedisTemplate.delete(cacheKey);
    }

    List<String> getAllKeys() {
        return stringRedisTemplate.keys("*").stream().toList();
    }
}
