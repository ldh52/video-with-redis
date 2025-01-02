package com.example.mytv.adapter.out;

import com.example.mytv.common.CacheNames;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Set;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class CacheManagePersistenceAdapterTest {
    private CacheManagePersistenceAdapter sut;

    private final StringRedisTemplate stringRedisTemplate = mock(StringRedisTemplate.class);

    @BeforeEach
    void setUp() {
        sut = new CacheManagePersistenceAdapter(stringRedisTemplate);
    }

    @Test
    void testGetAllCacheNames() {
        var result = sut.getAllCacheNames();

        then(result)
            .contains(CacheNames.CHANNEL, CacheNames.USER, CacheNames.VIDEO)
            .doesNotContain(CacheNames.SEPARATOR);
    }

    @Test
    void testGivenKeySetWhenGetAllCacheKeysThenReturnKeyList() {
        given(stringRedisTemplate.keys(any())).willReturn(Set.of("video", "video:like", "video:list"));
        var result = sut.getAllCacheNames("video");

        then(result)
            .contains("video", "video:like", "video:list");
    }

    @Test
    void testGivenNullWhenGetAllCacheKeysThenReturnEmptyList() {
        given(stringRedisTemplate.keys(any())).willReturn(null);
        var result = sut.getAllCacheNames("video");

        then(result)
            .isEmpty();
    }
}