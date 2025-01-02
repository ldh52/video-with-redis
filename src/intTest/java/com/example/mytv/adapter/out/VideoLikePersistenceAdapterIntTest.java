package com.example.mytv.adapter.out;

import static com.example.mytv.common.RedisKeyGenerator.getVideoLikeKey;
import static org.assertj.core.api.BDDAssertions.then;

import com.example.mytv.config.TestRedisConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest(classes = TestRedisConfig.class)
public class VideoLikePersistenceAdapterIntTest {
    @Autowired
    private VideoLikePersistenceAdapter sut;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Nested
    @DisplayName("videoId 로 생성된 VideoLike 키로 userId 를 set에 추가")
    class AddLikeVideo {
        @Test
        @DisplayName("add 하면 userId 가 추가된다")
        void testAddLikeVideo() {
            sut.addVideoLike("videoId", "userId");

            then(redisTemplate.opsForSet().isMember(getVideoLikeKey("videoId"), "userId"))
                .isTrue();
            then(redisTemplate.opsForSet().size(getVideoLikeKey("videoId")))
                .isEqualTo(1L);
        }

        @Test
        @DisplayName("여러번 add 해도 userId 가 중복 추가되지 않는다")
        void testMultipleAddLikeVideo() {
            for(int i = 0; i < 5; i++) {
                sut.addVideoLike("videoId", "userId");
            }

            then(redisTemplate.opsForSet().size(getVideoLikeKey("videoId")))
                .isEqualTo(1L);
        }
    }

    @Nested
    @DisplayName("videoId 로 생성된 VideoLike 키로 userId 를 set에서 제거")
    class RemoveLikeVideo {
        @Test
        @DisplayName("remove 하면 userId 가 제거된다")
        void testAddLikeVideo() {
            sut.removeVideoLike("videoId", "userId");

            then(redisTemplate.opsForSet().isMember(getVideoLikeKey("videoId"), "userId"))
                .isFalse();
        }
    }

    @Nested
    @DisplayName("videoId 로 생성된 VideoLike 키로 user 수 count")
    class CountLikeVideo {
        @Test
        @DisplayName("add 한 user 수 만큼 count 값 반환")
        void testAddLikeVideo() {
            for (int i = 0; i < 10; i++) {
                redisTemplate.opsForSet().add(getVideoLikeKey("videoId"), "userId" + i);
            }

            var result = sut.getVideoLikeCount("videoId");

            then(result).isEqualTo(10);
        }
    }
}
