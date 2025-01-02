package com.example.mytv.adapter.in;

import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.mytv.adapter.in.api.attribute.HeaderAttribute;
import com.example.mytv.adapter.in.api.dto.CommentRequest;
import com.example.mytv.adapter.out.jpa.user.UserJpaEntity;
import com.example.mytv.adapter.out.jpa.user.UserJpaRepository;
import com.example.mytv.adapter.out.mongo.comment.CommentMongoRepository;
import com.example.mytv.config.TestRedisConfig;
import com.example.mytv.common.RedisKeyGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = TestRedisConfig.class)
@AutoConfigureMockMvc
public class CommentApiControllerIntTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private CommentMongoRepository commentMongoRepository;

    private String authKey = UUID.randomUUID().toString();

    @BeforeEach
    void setUp() {
        userJpaRepository.save(new UserJpaEntity("userId", "user name", "https://exmaple.com/profile.jpg"));
        stringRedisTemplate.opsForValue().set(RedisKeyGenerator.getUserSessionKey(authKey), "userId");
    }

    @Test
    void testCreateComment() throws Exception{
        mockMvc
            .perform(
                post("/api/v1/comments")
                    .header(HeaderAttribute.X_AUTH_KEY, authKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(
                        new CommentRequest("channelId", "videoId", null, "댓글")
                    ))
            )
            .andExpectAll(
                status().isOk()
            );

        then(commentMongoRepository.count()).isEqualTo(1);
    }
}
