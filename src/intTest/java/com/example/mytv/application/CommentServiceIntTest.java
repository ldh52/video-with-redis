package com.example.mytv.application;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.example.mytv.adapter.out.jpa.user.UserJpaEntity;
import com.example.mytv.adapter.out.jpa.user.UserJpaRepository;
import com.example.mytv.adapter.out.mongo.comment.CommentDocument;
import com.example.mytv.adapter.out.redis.user.UserRedisHash;
import com.example.mytv.adapter.out.redis.user.UserRedisRepository;
import com.example.mytv.config.TestRedisConfig;
import com.example.mytv.domain.comment.Comment;
import com.example.mytv.domain.comment.CommentFixtures;
import com.example.mytv.domain.user.User;
import com.example.mytv.common.RedisKeyGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest(classes = TestRedisConfig.class)
public class CommentServiceIntTest {
    @Autowired
    private CommentService sut;

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private RedisTemplate<String, Long> redisTemplate;
    @SpyBean
    private UserJpaRepository userJpaRepository;
    @SpyBean
    private UserRedisRepository userRedisRepository;


    private Comment comment;
    private User author;

    @BeforeEach
    void setUp() {
        comment = CommentFixtures.stub("commentId");
        author = User.builder().id(comment.getAuthorId()).name("author").profileImageUrl("https://example.com/profile.jpg").build();

        mongoTemplate.save(CommentDocument.from(comment));
        redisTemplate.opsForValue().set(RedisKeyGenerator.getCommentLikeKey(comment.getId()), 20L);
        userJpaRepository.save(UserJpaEntity.from(author));
        userRedisRepository.deleteAll();
    }

    @Test
    @DisplayName("redis user 에 값이 없으면 DB에서 조회")
    void testGivenNoRedisUserWhenGetComment() {
        // When
        var result = sut.getComment("commentId");

        // Then
        then(result)
            .hasFieldOrPropertyWithValue("id", comment.getId())
            .hasFieldOrPropertyWithValue("videoId", comment.getVideoId())
            .hasFieldOrPropertyWithValue("text", comment.getText())
            .hasFieldOrPropertyWithValue("authorId", author.getId())
            .hasFieldOrPropertyWithValue("authorName", author.getName())
            .hasFieldOrPropertyWithValue("authorProfileImageUrl", author.getProfileImageUrl())
            .hasFieldOrPropertyWithValue("likeCount", 20L);

        verify(userJpaRepository).findById(author.getId());
        verify(userRedisRepository).findById(author.getId());
    }

    @Test
    @DisplayName("redis user 에 값이 있으면 Redis에서 조회")
    void testGivenRedisUserWhenGetComment() {
        // Given
        userRedisRepository.save(UserRedisHash.from(author));

        // When
        var result = sut.getComment("commentId");

        // Then
        then(result)
            .hasFieldOrPropertyWithValue("id", comment.getId())
            .hasFieldOrPropertyWithValue("videoId", comment.getVideoId())
            .hasFieldOrPropertyWithValue("text", comment.getText())
            .hasFieldOrPropertyWithValue("authorId", author.getId())
            .hasFieldOrPropertyWithValue("authorName", author.getName())
            .hasFieldOrPropertyWithValue("authorProfileImageUrl", author.getProfileImageUrl())
            .hasFieldOrPropertyWithValue("likeCount", 20L);

        verify(userRedisRepository).findById(author.getId());
        verify(userJpaRepository, never()).findById(author.getId());
    }
}
